package com.example.fakecryptoinvestorremake.domain.use_case.get_coin_price

import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.CoinDTO
import com.example.fakecryptoinvestorremake.data.remote.dto.toCoinPrice
import com.example.fakecryptoinvestorremake.domain.models.CoinType
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
    private val investmentRepository: InvestmentRepository
) {

    operator fun invoke(): Flow<Resource<List<CoinDTO>>> = flow {
        try {
            emit(Resource.Loading<List<CoinDTO>>())
            val coins = coinRepository.getCoins()
            profitUpdateUseCase(coins)
            emit(Resource.Success<List<CoinDTO>>(coins))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<CoinDTO>>(
                    e.localizedMessage ?: Constants.AN_UNEXPECTED_ERROR_OCCURED
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<CoinDTO>>("Couldn't reach server. Check your internet connection."))
        }
    }

    private suspend fun profitUpdateUseCase(coins: List<CoinDTO>) {
        try {
            investmentRepository.getInvestmentsList().onEach { investment ->
                var coinPrice = 0.0
                when (investment.symbol) {
                    CoinType.BTC.symbol -> {
                        coinPrice =
                            coins.find { it.symbol == CoinType.BTC.symbol }?.toCoinPrice()?.price
                                ?: 0.0
                    }
                    CoinType.ETH.symbol -> {
                        coinPrice =
                            coins.find { it.symbol == CoinType.ETH.symbol }?.toCoinPrice()?.price
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

        } catch (e: Exception) {

        }
    }
}