package com.example.fakecryptoinvestorremake.data.remote.repository

import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.data.remote.MessariApi
import com.example.fakecryptoinvestorremake.data.remote.dto.CoinDTO
import com.example.fakecryptoinvestorremake.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: MessariApi
): CoinRepository {

    override suspend fun getCoins(): List<CoinDTO> {
        return api.getCoins(Constants.FIELDS).data
    }
}