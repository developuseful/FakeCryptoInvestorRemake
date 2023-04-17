package com.example.fakecryptoinvestorremake.presentation.home

import com.example.fakecryptoinvestorremake.domain.models.Investment

data class HomeState(
    val isLoading: Boolean = false,
    val bitcoinPrice: Double? = null,
    val error: String = "",

    val investments: List<Investment> = emptyList(),

    val investName: String = "",
    val investAmount: Int = 0,
    val investHypothesis: String = "",
    val isAddingInvestment: Boolean = false,
)
