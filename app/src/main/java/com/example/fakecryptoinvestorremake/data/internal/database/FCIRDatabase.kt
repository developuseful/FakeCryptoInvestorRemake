package com.example.fakecryptoinvestorremake.data.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fakecryptoinvestorremake.data.internal.database.dao.InvestmentDao
import com.example.fakecryptoinvestorremake.domain.models.Investment

@Database(
    entities = [Investment::class],
    version = 12
)
abstract class FCIRDatabase: RoomDatabase() {

    abstract val investmentDao: InvestmentDao

    companion object{
        const val DATABASE_NAME = "investment_db"
    }
}