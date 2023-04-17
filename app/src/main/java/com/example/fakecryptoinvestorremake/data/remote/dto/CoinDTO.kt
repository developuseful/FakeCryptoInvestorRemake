package com.example.fakecryptoinvestorremake.data.remote.dto

import com.example.fakecryptoinvestorremake.domain.models.BitcoinPrice

data class CoinDTO(
    val id: String,
    val metrics: Metrics,
    val slug: String,
    val symbol: String
)

fun CoinDTO.toBitcoinPrice() : BitcoinPrice {
    return BitcoinPrice(
        price = metrics.market_data.price_usd
    )
}