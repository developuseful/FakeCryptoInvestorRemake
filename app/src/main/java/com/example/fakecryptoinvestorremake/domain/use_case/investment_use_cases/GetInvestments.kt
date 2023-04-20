package com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases

import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder
import com.example.fakecryptoinvestorremake.domain.util.OrderType
import kotlinx.coroutines.flow.*


class GetInvestments(
    private val investmentRepository: InvestmentRepository
) {
    operator fun invoke(
        investOrder: InvestOrder = InvestOrder.Date(OrderType.Descending)
    ): Flow<List<Investment>> {


        return investmentRepository.getInvestments().map { notes ->
            when (investOrder.orderType) {
                is OrderType.Ascending -> {
                    when (investOrder) {
                        is InvestOrder.Profit -> notes.sortedBy { it.profit }
                        is InvestOrder.Date -> notes.sortedBy { it.dateOfCreation }
                    }
                }
                is OrderType.Descending -> {
                    when (investOrder) {
                        is InvestOrder.Profit -> notes.sortedByDescending { it.profit }
                        is InvestOrder.Date -> notes.sortedByDescending { it.dateOfCreation }
                    }
                }

            }
        }


    }
}