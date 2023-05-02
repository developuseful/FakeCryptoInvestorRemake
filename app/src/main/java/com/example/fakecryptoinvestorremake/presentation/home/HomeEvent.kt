package com.example.fakecryptoinvestorremake.presentation.home

import com.example.fakecryptoinvestorremake.domain.util.InvestOrder

sealed interface HomeEvent{
    object SaveInvestment: HomeEvent

    data class Order(val investOrder: InvestOrder): HomeEvent
    object ToggleOrderSection: HomeEvent

    object UpdateBitcoinPrice: HomeEvent
    object ProfitUpdate: HomeEvent
}