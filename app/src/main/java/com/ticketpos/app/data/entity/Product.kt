package com.ticketpos.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["sku"], unique = true),
        Index(value = ["barcode"], unique = true),
        Index(value = ["category"])
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val description: String? = null,
    val sku: String, // Stock Keeping Unit
    val barcode: String? = null,
    val category: String,
    val brand: String? = null,
    
    val price: BigDecimal,
    val costPrice: BigDecimal? = null,
    val wholesalePrice: BigDecimal? = null,
    
    val stockQuantity: Int = 0,
    val minStockLevel: Int = 0,
    val maxStockLevel: Int? = null,
    
    val unit: String = "piece", // piece, kg, liter, etc.
    val weight: Double? = null, // in grams
    val dimensions: String? = null, // LxWxH in cm
    
    val isActive: Boolean = true,
    val isTaxable: Boolean = true,
    val taxRate: BigDecimal = BigDecimal.ZERO,
    
    val imageUrl: String? = null,
    val tags: String? = null, // comma-separated tags
    
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    
    val supplierId: Long? = null,
    val location: String? = null, // warehouse location
    
    val notes: String? = null
)