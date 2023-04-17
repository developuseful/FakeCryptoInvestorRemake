package com.example.fakecryptoinvestorremake.domain.repository

import com.example.fakecryptoinvestorremake.domain.models.Investment
import kotlinx.coroutines.flow.Flow

interface InvestmentRepository {

    fun getInvestments(): Flow<List<Investment>>

    suspend fun getInvestmentById(id: Int): Investment?

    suspend fun insertInvestment(investment: Investment)

    suspend fun deleteInvestment(investment: Investment)
}