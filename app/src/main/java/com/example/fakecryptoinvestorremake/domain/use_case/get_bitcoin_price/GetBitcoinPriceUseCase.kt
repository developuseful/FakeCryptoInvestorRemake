package com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price

import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.CoinDTO
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

    operator fun invoke(): Flow<Resource<List<CoinDTO>>> = flow {
        try {
            emit(Resource.Loading<List<CoinDTO>>())
            val coins = repository.getCoins()
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
}