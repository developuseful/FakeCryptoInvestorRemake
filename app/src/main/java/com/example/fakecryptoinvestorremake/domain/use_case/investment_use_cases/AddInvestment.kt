package com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases

import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository

class AddInvestment(
    private val investmentRepository: InvestmentRepository
) {

    suspend operator fun invoke(investment: Investment){
        investmentRepository.insertInvestment(investment = investment)
    }
}