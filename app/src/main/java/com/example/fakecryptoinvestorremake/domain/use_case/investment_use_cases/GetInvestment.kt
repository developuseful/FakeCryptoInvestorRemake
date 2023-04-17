package com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases

import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow

class GetInvestment(
    private val investmentRepository: InvestmentRepository
) {

    suspend operator fun invoke(id: Int): Investment? {
        return investmentRepository.getInvestmentById(id)
    }

}