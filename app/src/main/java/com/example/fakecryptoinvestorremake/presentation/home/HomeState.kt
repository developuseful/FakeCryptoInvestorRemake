package com.example.fakecryptoinvestorremake.presentation.home

import com.example.fakecryptoinvestorremake.data.remote.dto.CoinDTO
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder
import com.example.fakecryptoinvestorremake.domain.util.OrderType

data class HomeState(
    val isLoading: Boolean = false,
    val bitcoinPrice: Double? = null,
    val coins: List<CoinDTO>? = null,
    val error: String = "",

    val investments: List<Investment> = emptyList(),

    val investOrder: InvestOrder = InvestOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,

    val investName: String = "",
    val investAmount: String = "",
    val investHypothesis: String = "",
    val isAddingInvestment: Boolean = false,
)
