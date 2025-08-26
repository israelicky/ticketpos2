package com.ticketpos.app.data.repository

import android.content.SharedPreferences
import com.ticketpos.app.data.dao.UserDao
import com.ticketpos.app.data.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_CURRENT_USER = "current_user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    suspend fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun getUserById(id: Long): User? {
        return userDao.getUserById(id)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun authenticateUser(username: String, password: String): User? {
        val user = userDao.getUserByUsername(username)
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }

    fun saveUserSession(username: String) {
        sharedPreferences.edit()
            .putString(KEY_CURRENT_USER, username)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getCurrentUser(): User? {
        val username = sharedPreferences.getString(KEY_CURRENT_USER, null)
        return if (username != null) {
            // For demo purposes, return a mock user
            User(
                id = 1,
                username = username,
                password = "",
                fullName = "Administrador",
                role = "ADMIN",
                isActive = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        sharedPreferences.edit()
            .remove(KEY_CURRENT_USER)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }

    suspend fun createDefaultUsers() {
        // Create default admin user if not exists
        val adminUser = getUserByUsername("admin")
        if (adminUser == null) {
            val defaultAdmin = User(
                id = 0,
                username = "admin",
                password = "admin123",
                fullName = "Administrador",
                role = "ADMIN",
                isActive = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            insertUser(defaultAdmin)
        }
    }
}
