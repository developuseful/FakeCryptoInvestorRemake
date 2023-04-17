package com.example.fakecryptoinvestorremake.domain.repository

import com.example.fakecryptoinvestorremake.data.remote.dto.CoinDTO

interface CoinRepository {

    suspend fun getCoins() : List<CoinDTO>
}