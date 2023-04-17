package com.example.fakecryptoinvestorremake.data.internal.database.dao

import androidx.room.*
import com.example.fakecryptoinvestorremake.domain.models.Investment
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentDao {

    @Query("SELECT * FROM investment")
    fun getInvestments(): Flow<List<Investment>>

    @Query("SELECT * FROM investment WHERE id = :id")
    suspend fun getInvestmentById(id: Int): Investment

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestment(investment: Investment)

    @Delete
    suspend fun deleteInvestment(investment: Investment)
}