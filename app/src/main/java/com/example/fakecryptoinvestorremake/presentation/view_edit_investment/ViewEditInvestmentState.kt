package com.example.fakecryptoinvestorremake.presentation.view_edit_investment

import com.example.fakecryptoinvestorremake.domain.models.Investment

data class ViewEditInvestmentState(
    val currentInvestment: Investment? = null,
    val currentExchangeRate: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String = "",
    val isCommissionSectionVisible: Boolean = false,
    val purchaseCommission: String = "0",
    val salesCommission: String = "0"
)
