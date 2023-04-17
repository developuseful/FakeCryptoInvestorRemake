package com.example.fakecryptoinvestorremake.data.remote.dto

data class Result(
    val `data`: List<CoinDTO>,
    val status: Status
)