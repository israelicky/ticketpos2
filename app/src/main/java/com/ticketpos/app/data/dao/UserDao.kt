package com.ticketpos.app.data.dao

import androidx.room.*
import com.ticketpos.app.data.entity.User
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY firstName ASC")
    fun getAllActiveUsers(): Flow<List<User>>
    
    @Query("SELECT * FROM users ORDER BY firstName ASC")
    fun getAllUsers(): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): User?
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT * FROM users WHERE role = :role AND isActive = 1 ORDER BY firstName ASC")
    fun getUsersByRole(role: String): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE username = :username AND password = :password AND isActive = 1")
    suspend fun authenticateUser(username: String, password: String): User?
    
    @Query("SELECT COUNT(*) FROM users WHERE isActive = 1")
    suspend fun getActiveUsersCount(): Int
    
    @Query("SELECT COUNT(*) FROM users WHERE role = :role AND isActive = 1")
    suspend fun getUsersCountByRole(role: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("UPDATE users SET lastLoginAt = :lastLoginAt WHERE id = :userId")
    suspend fun updateLastLogin(userId: Long, lastLoginAt: Date)
    
    @Query("UPDATE users SET failedLoginAttempts = failedLoginAttempts + 1 WHERE id = :userId")
    suspend fun incrementFailedLoginAttempts(userId: Long)
    
    @Query("UPDATE users SET failedLoginAttempts = 0 WHERE id = :userId")
    suspend fun resetFailedLoginAttempts(userId: Long)
    
    @Query("UPDATE users SET isLocked = :isLocked, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateLockStatus(userId: Long, isLocked: Boolean, updatedAt: Date)
    
    @Query("UPDATE users SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateActiveStatus(userId: Long, isActive: Boolean, updatedAt: Date)
}