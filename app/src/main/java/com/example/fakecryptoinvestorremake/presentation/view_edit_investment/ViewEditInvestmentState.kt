package com.example.fakecryptoinvestorremake.presentation.view_edit_investment

import com.example.fakecryptoinvestorremake.domain.models.Investment

data class ViewEditInvestmentState(
    val investment: Investment? = null,
    val exchangeRate: Double = 0.0,
    val dateOfCreation: Long = System.currentTimeMillis(),
    val profit: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String = "",
)
