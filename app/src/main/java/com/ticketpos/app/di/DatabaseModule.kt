package com.ticketpos.app.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.ticketpos.app.data.dao.ProductDao
import com.ticketpos.app.data.dao.SaleDao
import com.ticketpos.app.data.dao.UserDao
import com.ticketpos.app.data.database.TicketPOSDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TicketPOSDatabase {
        return Room.databaseBuilder(
            context,
            TicketPOSDatabase::class.java,
            "ticketpos_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: TicketPOSDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideProductDao(database: TicketPOSDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideSaleDao(database: TicketPOSDatabase): SaleDao {
        return database.saleDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("ticketpos_prefs", Context.MODE_PRIVATE)
    }
}
