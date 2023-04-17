package com.example.fakecryptoinvestorremake.data.internal.repository

import com.example.fakecryptoinvestorremake.data.internal.database.dao.InvestmentDao
import com.example.fakecryptoinvestorremake.data.remote.MessariApi
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InvestmentRepositoryImp @Inject constructor(
    private val dao: InvestmentDao
) : InvestmentRepository {

    override fun getInvestments(): Flow<List<Investment>> {
        return dao.getInvestments()
    }

    override suspend fun getInvestmentById(id: Int): Investment {
        return dao.getInvestmentById(id)
    }

    override suspend fun insertInvestment(investment: Investment) {
        dao.insertInvestment(investment = investment)
    }

    override suspend fun deleteInvestment(investment: Investment) {
        dao.deleteInvestment(investment = investment)
    }


}