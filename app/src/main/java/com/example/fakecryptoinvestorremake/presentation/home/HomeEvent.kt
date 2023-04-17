package com.example.fakecryptoinvestorremake.presentation.home

sealed interface HomeEvent{
    object SaveInvestment: HomeEvent
    data class SetInvestName(val investName: String) : HomeEvent
    data class SetInvestAmount(val investAmount: Int) : HomeEvent
    data class SetInvestHypothesis(val investHypothesis: String) : HomeEvent
    object ShowDialog: HomeEvent
    object HideDialog: HomeEvent
}