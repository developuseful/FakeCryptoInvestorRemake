package com.example.fakecryptoinvestorremake.domain.use_case

import com.example.fakecryptoinvestorremake.data.remote.dto.toCoinPrice
import com.example.fakecryptoinvestorremake.domain.models.CoinType
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository


class ProfitUpdateUseCase(
    private val investmentRepository: InvestmentRepository,
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke() {
        try {
            coinRepository.getCoins().let { coins ->
                investmentRepository.getInvestmentsList().onEach { investment ->
                    var coinPrice = 0.0
                    when(investment.symbol){
                        CoinType.BTC.symbol -> {
                            coinPrice = coins.find { it.symbol == CoinType.BTC.symbol }?.toCoinPrice()?.price
                                ?: 0.0
                        }
                        CoinType.ETH.symbol -> {
                            coinPrice = coins.find { it.symbol == CoinType.ETH.symbol }?.toCoinPrice()?.price
                                ?: 0.0
                        }
                    }
                    val exchangeRateVolatility = 100 - (investment.exchangeRate * 100 / coinPrice)
                    investmentRepository.insertInvestment(
                        investment.copy(
                            exchangeRateVolatility = exchangeRateVolatility,
                            profitPercentage = (-investment.purchaseCommission.toDouble() + exchangeRateVolatility) - investment.salesCommission
                        )
                    )
                }
            }
        } catch (e: Exception){

        }

    }
}