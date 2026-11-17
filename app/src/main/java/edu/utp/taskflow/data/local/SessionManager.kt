package edu.utp.taskflow.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Guarda qué usuario tiene la sesión abierta.
 * Se persiste en SharedPreferences para que, si se cierra la app,
 * el usuario siga con la sesión iniciada al volver a abrirla.
 */
class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("taskflow_session", Context.MODE_PRIVATE)

    private val _currentUserId = MutableStateFlow(readUserId())
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    private val _currentUsername = MutableStateFlow(prefs.getString(KEY_USERNAME, null))
    val currentUsername: StateFlow<String?> = _currentUsername.asStateFlow()

    fun login(userId: Int, username: String) {
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .apply()
        _currentUsername.value = username
        _currentUserId.value = userId
    }

    fun logout() {
        prefs.edit().clear().apply()
        _currentUserId.value = null
        _currentUsername.value = null
    }

    private fun readUserId(): Int? {
        val id = prefs.getInt(KEY_USER_ID, NO_USER)
        return if (id == NO_USER) null else id
    }

    private companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_USERNAME = "username"
        const val NO_USER = -1
    }
}
