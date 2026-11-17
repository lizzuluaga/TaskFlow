package edu.utp.taskflow.data.repository

import edu.utp.taskflow.data.local.SessionManager
import edu.utp.taskflow.data.local.TaskDao
import edu.utp.taskflow.data.local.TaskEntity
import edu.utp.taskflow.model.Task
import edu.utp.taskflow.model.TaskPriority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TaskRepository(
    private val dao: TaskDao,
    private val session: SessionManager
) {

    // Cada vez que cambia el usuario de la sesión, se observan sus tareas.
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks: Flow<List<Task>> =
        session.currentUserId.flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                dao.observeByUser(userId).map { entities ->
                    entities.map { entity -> entity.toModel() }
                }
            }
        }

    suspend fun addTask(
        title: String,
        priority: TaskPriority
    ) {
        val userId = session.currentUserId.value ?: return
        val cleanTitle = title.trim()

        if (cleanTitle.isBlank()) return

        val entity = TaskEntity(
            userId = userId,
            title = cleanTitle,
            category = "Personal",
            priority = priority.name
        )

        dao.insert(entity)
    }

    suspend fun toggleTask(taskId: Int) {
        val userId = session.currentUserId.value ?: return
        val entity = dao.findById(taskId, userId) ?: return

        dao.update(
            entity.copy(
                isCompleted = !entity.isCompleted
            )
        )
    }

    suspend fun deleteTask(taskId: Int) {
        val userId = session.currentUserId.value ?: return
        val entity = dao.findById(taskId, userId) ?: return

        dao.delete(entity)
    }
}


private fun TaskEntity.toModel(): Task {
    return Task(
        id = id,
        title = title,
        category = category,
        priority =
            TaskPriority.entries.firstOrNull {
                it.name == priority
            } ?: TaskPriority.MEDIUM,
        isCompleted = isCompleted
    )
}
