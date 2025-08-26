package com.ticketpos.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "customers",
    indices = [
        Index(value = ["phone"], unique = true),
        Index(value = ["email"], unique = true),
        Index(value = ["customerCode"])
    ]
)
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val customerCode: String, // Unique customer identifier
    val firstName: String,
    val lastName: String,
    val fullName: String,
    
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    
    val birthDate: Date? = null,
    val gender: String? = null, // male, female, other
    
    val customerType: String = "regular", // regular, vip, wholesale, etc.
    val creditLimit: BigDecimal? = null,
    val currentBalance: BigDecimal = BigDecimal.ZERO,
    
    val taxExempt: Boolean = false,
    val taxId: String? = null,
    
    val notes: String? = null,
    val tags: String? = null, // comma-separated tags
    
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val createdBy: Long? = null,
    val updatedBy: Long? = null
)