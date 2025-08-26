package com.ticketpos.app.data.dao

import androidx.room.*
import com.ticketpos.app.data.entity.Product
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?
    
    @Query("SELECT * FROM products WHERE sku = :sku")
    suspend fun getProductBySku(sku: String): Product?
    
    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): Product?
    
    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE category = :category AND isActive = 1 ORDER BY name ASC")
    fun getProductsByCategory(category: String): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE stockQuantity <= minStockLevel AND isActive = 1 ORDER BY stockQuantity ASC")
    fun getLowStockProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE stockQuantity = 0 AND isActive = 1 ORDER BY name ASC")
    fun getOutOfStockProducts(): Flow<List<Product>>
    
    @Query("SELECT DISTINCT category FROM products WHERE isActive = 1 ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>
    
    @Query("SELECT COUNT(*) FROM products WHERE isActive = 1")
    suspend fun getActiveProductsCount(): Int
    
    @Query("SELECT COUNT(*) FROM products WHERE stockQuantity <= minStockLevel AND isActive = 1")
    suspend fun getLowStockProductsCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("UPDATE products SET stockQuantity = stockQuantity + :quantity WHERE id = :productId")
    suspend fun addStock(productId: Long, quantity: Int)
    
    @Query("UPDATE products SET stockQuantity = stockQuantity - :quantity WHERE id = :productId AND stockQuantity >= :quantity")
    suspend fun removeStock(productId: Long, quantity: Int): Int
    
    @Query("UPDATE products SET price = :newPrice, updatedAt = :updatedAt WHERE id = :productId")
    suspend fun updatePrice(productId: Long, newPrice: BigDecimal, updatedAt: java.util.Date)
    
    @Query("UPDATE products SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :productId")
    suspend fun updateActiveStatus(productId: Long, isActive: Boolean, updatedAt: java.util.Date)
}