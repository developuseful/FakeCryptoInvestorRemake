package com.example.fakecryptoinvestorremake.presentation.home

import com.example.fakecryptoinvestorremake.domain.models.CoinType
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder

sealed interface HomeEvent{
    data class SaveInvestment(val coinType: CoinType): HomeEvent

    data class Order(val investOrder: InvestOrder): HomeEvent
    object ToggleOrderSection: HomeEvent

    object ToggleAddSection: HomeEvent

    object UpdateCoinPrice: HomeEvent
    object ProfitUpdate: HomeEvent
}

