package edu.utp.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import edu.utp.taskflow.data.sampleTasks
import edu.utp.taskflow.model.Task
import edu.utp.taskflow.model.TaskFilter
import edu.utp.taskflow.model.TaskPriority
import edu.utp.taskflow.ui.screens.TaskFlowScreen
import edu.utp.taskflow.ui.theme.TaskFlowPalette
import edu.utp.taskflow.ui.theme.TaskFlowTheme
import edu.utp.taskflow.ui.components.NewTaskForm
import edu.utp.taskflow.viewmodel.TaskViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import edu.utp.taskflow.data.local.TaskDatabase
import edu.utp.taskflow.data.remote.RetrofitClient
import edu.utp.taskflow.data.repository.RemoteTaskRepository
import edu.utp.taskflow.data.repository.TaskRepository
import edu.utp.taskflow.ui.screens.RemoteTasksScreen
import edu.utp.taskflow.ui.screens.TaskDetailScreen
import edu.utp.taskflow.viewmodel.RemoteTaskViewModel
import edu.utp.taskflow.viewmodel.RemoteTaskViewModelFactory

import edu.utp.taskflow.viewmodel.TaskViewModelFactory
import edu.utp.taskflow.viewmodel.AuthViewModel
import edu.utp.taskflow.viewmodel.AuthViewModelFactory
import edu.utp.taskflow.data.local.SessionManager
import edu.utp.taskflow.data.repository.AuthRepository
import edu.utp.taskflow.ui.screens.AuthScreen
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.rememberSaveable


private object TaskRoutes {
    const val LIST = "tasks"
    const val REMOTE = "remote_tasks"
    const val DETAIL = "task_detail/{taskId}"

    fun detail(taskId: Int): String {
        return "task_detail/$taskId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Config ROOM
        val database = TaskDatabase.getDatabase(applicationContext)
        val session = SessionManager(applicationContext)

        val repository = TaskRepository(database.taskDao(), session)
        val factory = TaskViewModelFactory(repository)

        val authRepository = AuthRepository(database.userDao(), session)
        val authFactory = AuthViewModelFactory(authRepository)

        // Config Retrofit
        val api = RetrofitClient.api
        val remoteRepository = RemoteTaskRepository(api)
        val remoteFactory =
            RemoteTaskViewModelFactory(
                remoteRepository
            )

        setContent {
            val taskViewModel: TaskViewModel = viewModel(factory = factory)
            val authViewModel: AuthViewModel = viewModel(factory = authFactory)

            val currentUserId by authViewModel.currentUserId.collectAsStateWithLifecycle()
            val currentUsername by authViewModel.currentUsername.collectAsStateWithLifecycle()

            if (currentUserId == null) {
                // Sin sesión: pedir login o registro
                AuthGate(authViewModel)
            } else {
                // key: si cambia el usuario se reinicia la navegación
                key(currentUserId) {
                    TaskFlowDemo(
                        taskViewModel = taskViewModel,
                        remoteFactory = remoteFactory,
                        username = currentUsername,
                        onLogout = authViewModel::logout
                    )
                }
            }
        }
    }
}

@Composable
fun AuthGate(authViewModel: AuthViewModel) {
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()

    var isRegister by rememberSaveable { mutableStateOf(false) }

    TaskFlowTheme(palette = TaskFlowPalette.LAVANDA) {
        Scaffold { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                AuthScreen(
                    isRegister = isRegister,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onLogin = authViewModel::login,
                    onRegister = authViewModel::register,
                    onSwitchMode = {
                        authViewModel.clearError()
                        isRegister = !isRegister
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFlowDemo(
    taskViewModel: TaskViewModel = viewModel(),
    remoteFactory: RemoteTaskViewModelFactory,
    username: String? = null,
    onLogout: () -> Unit = {}
) {
    val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()
    val selectedFilter by taskViewModel.selectedFilter.collectAsStateWithLifecycle()

    // Único estado interactivo de la base visual: cambiar la paleta.
    // Las operaciones sobre tareas siguen pendientes para la clase.
    var palette by remember { mutableStateOf(TaskFlowPalette.LAVANDA) }

    var showNewTaskSheet by remember {
        mutableStateOf(false)
    }

    var newTaskTitle by remember {
        mutableStateOf("")
    }

    var newTaskPriority by remember {
        mutableStateOf(TaskPriority.MEDIUM)
    }

    val navController = rememberNavController()

    val backStackEntry by navController
        .currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route



    TaskFlowTheme(palette = palette) {
        Scaffold(
            bottomBar = {
                if (
                    currentRoute == TaskRoutes.LIST ||
                    currentRoute == TaskRoutes.REMOTE
                ) {
                    NavigationBar {

                        // OPCIÓN LOCAL
                        NavigationBarItem(

                            selected =
                                currentRoute == TaskRoutes.LIST,

                            onClick = {

                                navController.navigate(
                                    TaskRoutes.LIST
                                ) {

                                    popUpTo(TaskRoutes.LIST) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            icon = {
                                Icon(
                                    imageVector =
                                        Icons.Filled.Home,

                                    contentDescription =
                                        "Tareas locales"
                                )
                            },

                            label = {
                                Text("Local")
                            }
                        )

                        // OPCIÓN REMOTO
                        NavigationBarItem(

                            selected =
                                currentRoute == TaskRoutes.REMOTE,

                            onClick = {

                                navController.navigate(
                                    TaskRoutes.REMOTE
                                ) {

                                    popUpTo(TaskRoutes.LIST) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            icon = {
                                Icon(
                                    imageVector =
                                        Icons.Filled.Cloud,

                                    contentDescription =
                                        "Tareas remotas"
                                )
                            },

                            label = {
                                Text("Remoto")
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = TaskRoutes.LIST
                ) {

                    // Pantalla principal
                    composable(TaskRoutes.LIST) {
                        TaskFlowScreen(
                            tasks = tasks,
                            selectedFilter = selectedFilter,
                            onFilterChange = taskViewModel::changeFilter,
                            onToggleTask = taskViewModel::toggleTask,
                            onDeleteTask = taskViewModel::deleteTask,
                            onTaskClick = { taskId ->
                                navController.navigate(
                                    TaskRoutes.detail(taskId)
                                )
                            },
                            onNewTaskClick = {
                                newTaskTitle = ""
                                newTaskPriority = TaskPriority.MEDIUM
                                showNewTaskSheet = true
                            },
                            palette = palette,
                            onPaletteChange = { palette = it },
                            username = username,
                            onLogout = onLogout
                        )
                    }

                    // Pantalla de datos remotos
                    composable(TaskRoutes.REMOTE) {
                        val remoteViewModel:
                                RemoteTaskViewModel =
                            viewModel(
                                factory = remoteFactory
                            )

                        val remoteTasks by
                        remoteViewModel.tasks
                            .collectAsStateWithLifecycle()

                        val isLoading by
                        remoteViewModel.isLoading
                            .collectAsStateWithLifecycle()

                        val errorMessage by
                        remoteViewModel.errorMessage
                            .collectAsStateWithLifecycle()

                        RemoteTasksScreen(
                            tasks = remoteTasks,
                            isLoading = isLoading,
                            errorMessage = errorMessage,
                            onRetry = {
                                remoteViewModel.loadTasks()
                            }
                        )
                    }

                    // Pantalla de detalle
                    composable(
                        route = TaskRoutes.DETAIL,
                        arguments = listOf(
                            navArgument("taskId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getInt("taskId")

                        val selectedTask = tasks.firstOrNull {
                            it.id == taskId
                        }

                        TaskDetailScreen(
                            task = selectedTask,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }

        }

        if (showNewTaskSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showNewTaskSheet = false
                }
            ) {
                NewTaskForm(
                    title = newTaskTitle,
                    onTitleChange = { newTaskTitle = it },
                    selectedPriority = newTaskPriority,
                    onPriorityChange = { newTaskPriority = it },
                    onSave = {
                        if (newTaskTitle.isNotBlank()) {
                            taskViewModel.addTask(
                                title = newTaskTitle,
                                priority = newTaskPriority
                            )

                            showNewTaskSheet = false
                        }
                    },
                    onCancel = {
                        showNewTaskSheet = false
                    }
                )
            }
        }

    }
}