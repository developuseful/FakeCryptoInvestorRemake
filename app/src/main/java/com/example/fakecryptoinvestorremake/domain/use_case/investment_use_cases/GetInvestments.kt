package com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases

import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder
import com.example.fakecryptoinvestorremake.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetInvestments(
    private val investmentRepository: InvestmentRepository
) {
    operator fun invoke(
        investOrder: InvestOrder = InvestOrder.Date(OrderType.Descending)
    ): Flow<List<Investment>> {


        return investmentRepository.getInvestments().map { investments ->
            when (investOrder.orderType) {
                is OrderType.Ascending -> {
                    when (investOrder) {
                        is InvestOrder.Profit -> investments.sortedBy { it.profit }
                        is InvestOrder.Date -> investments.sortedBy { it.dateOfCreation }
                        is InvestOrder.Id -> investments.sortedBy { it.id }
                    }
                }
                is OrderType.Descending -> {
                    when (investOrder) {
                        is InvestOrder.Profit -> investments.sortedByDescending { it.profit }
                        is InvestOrder.Date -> investments.sortedByDescending { it.dateOfCreation }
                        is InvestOrder.Id -> investments.sortedByDescending { it.id }
                    }
                }

            }
        }


    }
}