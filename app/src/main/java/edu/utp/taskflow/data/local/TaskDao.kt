package edu.utp.taskflow.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO: Data Access Object
/*
* CRUD
* C - Create
* R - Read
* U - Update
* D - Delete
* */

@Dao
interface TaskDao {
    // Solo las tareas del usuario indicado
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY id DESC")
    fun observeByUser(userId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId AND userId = :userId LIMIT 1")
    suspend fun findById(taskId: Int, userId: Int): TaskEntity?

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}
