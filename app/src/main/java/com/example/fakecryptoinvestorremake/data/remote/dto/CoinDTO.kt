package com.example.fakecryptoinvestorremake.data.remote.dto

import com.example.fakecryptoinvestorremake.domain.models.CoinPrice

data class CoinDTO(
    val id: String,
    val metrics: Metrics,
    val slug: String,
    val symbol: String
)

fun CoinDTO.toCoinPrice() : CoinPrice {
    return CoinPrice(
        price = metrics.market_data.price_usd
    )
}