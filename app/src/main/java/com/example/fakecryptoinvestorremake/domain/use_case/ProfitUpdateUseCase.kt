package com.example.fakecryptoinvestorremake.domain.use_case

import android.util.Log
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import kotlinx.coroutines.flow.onEach

class ProfitUpdateUseCase(
    private val investmentRepository: InvestmentRepository,
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke() {

        Log.d("ProfitUpdateUseCase", "1111")
        val bitcoinPrice = coinRepository.getCoins()[0].toBitcoinPrice().price

        val list = investmentRepository.getInvestmentsList()

        Log.d("ProfitUpdateUseCase", list.toString())

        Log.d("ProfitUpdateUseCase", "2222")


        for (inv in list) {
            investmentRepository.insertInvestment(
                Investment(
                    id = inv.id,
                    name = inv.name,
                    value = inv.value,
                    hypothesis = inv.hypothesis,
                    dateOfCreation = inv.dateOfCreation,
                    profit = 100 - (inv.value * 100 / bitcoinPrice)
                )
            )
            Log.d("ProfitUpdateUseCase", inv.toString())
        }



        Log.d("ProfitUpdateUseCase", "9999")

    }
}