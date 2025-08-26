package com.ticketpos.app.data.dao

import androidx.room.*
import com.ticketpos.app.data.entity.SaleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao {
    
    @Query("SELECT * FROM sale_items WHERE saleId = :saleId ORDER BY id ASC")
    fun getSaleItemsBySaleId(saleId: Long): Flow<List<SaleItem>>
    
    @Query("SELECT * FROM sale_items WHERE productId = :productId ORDER BY id DESC")
    fun getSaleItemsByProductId(productId: Long): Flow<List<SaleItem>>
    
    @Query("SELECT * FROM sale_items WHERE id = :id")
    suspend fun getSaleItemById(id: Long): SaleItem?
    
    @Query("SELECT COUNT(*) FROM sale_items WHERE saleId = :saleId")
    suspend fun getSaleItemsCount(saleId: Long): Int
    
    @Query("SELECT SUM(quantity) FROM sale_items WHERE productId = :productId")
    suspend fun getTotalQuantitySoldForProduct(productId: Long): Double?
    
    @Query("SELECT SUM(totalPrice) FROM sale_items WHERE productId = :productId")
    suspend fun getTotalRevenueForProduct(productId: Long): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItem(saleItem: SaleItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItems(saleItems: List<SaleItem>)
    
    @Update
    suspend fun updateSaleItem(saleItem: SaleItem)
    
    @Delete
    suspend fun deleteSaleItem(saleItem: SaleItem)
    
    @Query("DELETE FROM sale_items WHERE saleId = :saleId")
    suspend fun deleteSaleItemsBySaleId(saleId: Long)
}