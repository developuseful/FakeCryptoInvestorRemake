package com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price

import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBitcoinPriceUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    operator fun invoke(): Flow<Resource<Double>> = flow {

        try {
            emit(Resource.Loading<Double>())
            val bitcoinPrice = repository.getCoins().get(0).toBitcoinPrice().price
            emit(Resource.Success<Double>(bitcoinPrice))
        } catch (e: HttpException) {
            emit(
                Resource.Error<Double>(
                    e.localizedMessage ?: Constants.AN_UNEXPECTED_ERROR_OCCURED
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<Double>("Couldn't reach server. Check your internet connection."))
        }
    }
}