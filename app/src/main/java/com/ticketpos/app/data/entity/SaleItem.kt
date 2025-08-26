package com.ticketpos.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import java.math.BigDecimal

@Entity(
    tableName = "sale_items",
    foreignKeys = [
        ForeignKey(
            entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["saleId"]),
        Index(value = ["productId"])
    ]
)
data class SaleItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val saleId: Long,
    val productId: Long,
    
    val productName: String,
    val productSku: String,
    val productBarcode: String? = null,
    
    val quantity: Double,
    val unit: String = "piece",
    
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
    
    val discountPercentage: BigDecimal = BigDecimal.ZERO,
    val discountAmount: BigDecimal = BigDecimal.ZERO,
    
    val taxRate: BigDecimal = BigDecimal.ZERO,
    val taxAmount: BigDecimal = BigDecimal.ZERO,
    
    val costPrice: BigDecimal? = null,
    val profit: BigDecimal? = null,
    
    val notes: String? = null
)