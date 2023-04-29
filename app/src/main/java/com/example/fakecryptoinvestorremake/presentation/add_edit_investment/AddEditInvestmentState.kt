package com.example.fakecryptoinvestorremake.presentation.add_edit_investment

data class AddEditInvestmentState(
    val exchangeRate: Double = 0.0,
    val dateOfCreation: Long = System.currentTimeMillis(),
    val profit: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String = "",
)
