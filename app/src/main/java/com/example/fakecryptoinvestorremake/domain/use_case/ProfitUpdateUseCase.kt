package com.example.fakecryptoinvestorremake.domain.use_case

import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository

class ProfitUpdateUseCase(
    private val investmentRepository: InvestmentRepository,
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke() {
        try {
            coinRepository.getCoins()[0].toBitcoinPrice().price.let { bitcoinPrice ->
                investmentRepository.getInvestmentsList().onEach { investment ->
                    investmentRepository.insertInvestment(
                        investment.copy(profit = 100 - (investment.exchangeRate * 100 / bitcoinPrice))
                    )
                }
            }
        } catch (e: Exception){

        }

    }
}