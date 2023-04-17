package com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases

data class InvestmentUseCases(
    val getInvestments: GetInvestments,
    val getInvestment: GetInvestment,
    val addInvestment: AddInvestment,
    val deleteInvestment: DeleteInvestment
)
