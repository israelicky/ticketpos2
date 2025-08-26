package com.ticketpos.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true),
        Index(value = ["role"])
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val username: String,
    val password: String, // Hashed password
    val email: String? = null,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    
    val role: String, // admin, manager, cashier, etc.
    val permissions: String? = null, // JSON string of permissions
    
    val phone: String? = null,
    val address: String? = null,
    val employeeId: String? = null,
    
    val isActive: Boolean = true,
    val isLocked: Boolean = false,
    val failedLoginAttempts: Int = 0,
    val lastLoginAt: Date? = null,
    
    val profileImageUrl: String? = null,
    val notes: String? = null,
    
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val createdBy: Long? = null,
    val updatedBy: Long? = null
)