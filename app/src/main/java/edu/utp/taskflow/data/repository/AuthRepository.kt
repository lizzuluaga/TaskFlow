package edu.utp.taskflow.data.repository

import android.database.sqlite.SQLiteConstraintException
import edu.utp.taskflow.data.local.SessionManager
import edu.utp.taskflow.data.local.UserDao
import edu.utp.taskflow.data.local.UserEntity
import java.security.MessageDigest
import java.security.SecureRandom

sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(
    private val userDao: UserDao,
    private val session: SessionManager
) {

    val currentUserId = session.currentUserId
    val currentUsername = session.currentUsername

    suspend fun register(username: String, password: String): AuthResult {
        val cleanUser = username.trim().lowercase()

        if (cleanUser.length < 3) {
            return AuthResult.Error("El usuario debe tener al menos 3 caracteres")
        }
        if (password.length < 4) {
            return AuthResult.Error("La contraseña debe tener al menos 4 caracteres")
        }
        if (userDao.findByUsername(cleanUser) != null) {
            return AuthResult.Error("Ese usuario ya existe")
        }

        val salt = newSalt()
        val user = UserEntity(
            username = cleanUser,
            passwordHash = hash(password, salt),
            salt = salt
        )

        val newId = try {
            userDao.insert(user).toInt()
        } catch (e: SQLiteConstraintException) {
            return AuthResult.Error("Ese usuario ya existe")
        }

        // Al registrarse queda con la sesión iniciada
        session.login(newId, cleanUser)
        return AuthResult.Success
    }

    suspend fun login(username: String, password: String): AuthResult {
        val cleanUser = username.trim().lowercase()
        val user = userDao.findByUsername(cleanUser)

        if (user == null || hash(password, user.salt) != user.passwordHash) {
            return AuthResult.Error("Usuario o contraseña incorrectos")
        }

        session.login(user.id, user.username)
        return AuthResult.Success
    }

    fun logout() {
        session.logout()
    }

    private fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.toHex()
    }

    private fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest((salt + password).toByteArray()).toHex()
    }

    private fun ByteArray.toHex(): String =
        joinToString("") { "%02x".format(it) }
}
