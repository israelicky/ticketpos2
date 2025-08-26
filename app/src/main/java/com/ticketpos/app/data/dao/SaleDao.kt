package com.ticketpos.app.data.dao

import androidx.room.*
import com.ticketpos.app.data.entity.Sale
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SaleDao {
    
    @Query("SELECT * FROM sales ORDER BY date DESC")
    fun getAllSales(): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: Long): Sale?
    
    @Query("SELECT * FROM sales WHERE saleNumber = :saleNumber")
    suspend fun getSaleByNumber(saleNumber: String): Sale?
    
    @Query("SELECT * FROM sales WHERE userId = :userId ORDER BY date DESC")
    fun getSalesByUser(userId: Long): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE customerId = :customerId ORDER BY date DESC")
    fun getSalesByCustomer(customerId: Long): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE date >= :startDate ORDER BY date DESC")
    fun getSalesFromDate(startDate: Date): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getSalesInDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE paymentStatus = :status ORDER BY date DESC")
    fun getSalesByPaymentStatus(status: String): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE isVoid = 0 ORDER BY date DESC")
    fun getValidSales(): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE isVoid = 1 ORDER BY date DESC")
    fun getVoidedSales(): Flow<List<Sale>>
    
    @Query("SELECT COUNT(*) FROM sales WHERE date >= :startDate AND date <= :endDate")
    suspend fun getSalesCountInDateRange(startDate: Date, endDate: Date): Int
    
    @Query("SELECT COUNT(*) FROM sales WHERE date >= :startDate AND date <= :endDate AND isVoid = 0")
    suspend fun getValidSalesCountInDateRange(startDate: Date, endDate: Date): Int
    
    @Query("SELECT SUM(totalAmount) FROM sales WHERE date >= :startDate AND date <= :endDate AND isVoid = 0")
    suspend fun getTotalSalesAmountInDateRange(startDate: Date, endDate: Date): Double?
    
    @Query("SELECT SUM(totalAmount) FROM sales WHERE date >= :startDate AND date <= :endDate AND isVoid = 0 AND paymentStatus = 'completed'")
    suspend fun getCompletedSalesAmountInDateRange(startDate: Date, endDate: Date): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSales(sales: List<Sale>)
    
    @Update
    suspend fun updateSale(sale: Sale)
    
    @Delete
    suspend fun deleteSale(sale: Sale)
    
    @Query("UPDATE sales SET isVoid = 1, voidReason = :reason, voidedBy = :userId, voidedAt = :voidedAt WHERE id = :saleId")
    suspend fun voidSale(saleId: Long, reason: String, userId: Long, voidedAt: Date)
    
    @Query("UPDATE sales SET receiptPrinted = 1, receiptNumber = :receiptNumber WHERE id = :saleId")
    suspend fun markReceiptPrinted(saleId: Long, receiptNumber: String)
    
    @Query("UPDATE sales SET paymentStatus = :status, updatedAt = :updatedAt WHERE id = :saleId")
    suspend fun updatePaymentStatus(saleId: Long, status: String, updatedAt: Date)
}