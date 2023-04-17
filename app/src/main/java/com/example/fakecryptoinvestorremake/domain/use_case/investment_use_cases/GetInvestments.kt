package com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases

import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow


class GetInvestments(
    private val investmentRepository: InvestmentRepository
) {

    operator fun invoke(): Flow<List<Investment>> {
        return investmentRepository.getInvestments()
    }


//    operator fun invoke(): Flow<Resource<List<Investment>>> = flow {
//
//        try {
//            emit(Resource.Loading<List<Investment>>())
//            val investments = investmentRepository.getInvestments()
//            emit(Resource.Success<List<Investment>>(investments))
//        } catch (e: HttpException) {
//            emit(
//                Resource.Error<List<Investment>>(
//                    e.localizedMessage ?: Constants.AN_UNEXPECTED_ERROR_OCCURED
//                )
//            )
//        } catch (e: IOException) {
//            emit(Resource.Error<List<Investment>>("Couldn't reach server. Check your internet connection."))
//        }
//    }
}