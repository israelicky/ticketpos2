package com.ticketpos.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.ticketpos.app.data.entity.*
import com.ticketpos.app.data.dao.*

@Database(
    entities = [
        Product::class,
        Sale::class,
        SaleItem::class,
        User::class,
        Customer::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TicketPOSDatabase : RoomDatabase() {

    // DAOs
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun userDao(): UserDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: TicketPOSDatabase? = null

        fun getDatabase(context: Context): TicketPOSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TicketPOSDatabase::class.java,
                    "ticketpos_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}