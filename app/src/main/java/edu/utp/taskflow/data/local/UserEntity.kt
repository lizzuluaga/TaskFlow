package edu.utp.taskflow.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,

    // Nunca guardamos la contraseña en texto plano: solo su hash + sal.
    val passwordHash: String,

    val salt: String
)
