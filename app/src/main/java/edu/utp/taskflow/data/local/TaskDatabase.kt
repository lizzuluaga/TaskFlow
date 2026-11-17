package edu.utp.taskflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [TaskEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(
            context: Context
        ): TaskDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "taskflow_database"
                )
                    // Al pasar de la versión 1 a la 2 (se agregan usuarios)
                    // se recrea la base de datos. Las tareas viejas, que no
                    // tenían dueño, se borran.
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
