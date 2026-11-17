package edu.utp.taskflow.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Dueño de la tarea
    val userId: Int,

    val title: String,

    val category: String,

    val priority: String,

    val isCompleted: Boolean = false
)
