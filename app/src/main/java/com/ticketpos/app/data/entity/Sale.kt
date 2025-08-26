package com.ticketpos.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "sales",
    indices = [
        Index(value = ["saleNumber"]),
        Index(value = ["customerId"]),
        Index(value = ["userId"]),
        Index(value = ["date"])
    ]
)
data class Sale(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val saleNumber: String, // Unique sale identifier
    val date: Date = Date(),
    
    val customerId: Long? = null,
    val customerName: String? = null,
    val customerPhone: String? = null,
    val customerEmail: String? = null,
    
    val userId: Long, // Cashier/User who made the sale
    val userName: String,
    
    val subtotal: BigDecimal = BigDecimal.ZERO,
    val taxAmount: BigDecimal = BigDecimal.ZERO,
    val discountAmount: BigDecimal = BigDecimal.ZERO,
    val totalAmount: BigDecimal = BigDecimal.ZERO,
    
    val paymentMethod: String, // cash, card, transfer, etc.
    val paymentStatus: String = "completed", // pending, completed, failed, refunded
    val transactionId: String? = null, // Payment gateway transaction ID
    
    val itemsCount: Int = 0,
    val notes: String? = null,
    
    val isVoid: Boolean = false,
    val voidReason: String? = null,
    val voidedBy: Long? = null,
    val voidedAt: Date? = null,
    
    val receiptPrinted: Boolean = false,
    val receiptNumber: String? = null,
    
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)