package com.example.fakecryptoinvestorremake.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.use_case.ProfitUpdateUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price.GetBitcoinPriceUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.InvestmentUseCases
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder
import com.example.fakecryptoinvestorremake.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBitcoinPriceUseCase: GetBitcoinPriceUseCase,
    private val investmentUseCases: InvestmentUseCases,
    private val profitUpdateUseCase: ProfitUpdateUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private var getInvestmentsJob: Job? = null


    init {
        viewModelScope.launch { profitUpdateUseCase.invoke() }
        getBitcoinPrice()
        getInvestments(InvestOrder.Date(OrderType.Descending))
    }


    suspend fun profitUpdateUseCase() {
        profitUpdateUseCase.invoke()
    }

    private fun getBitcoinPrice() {
        getBitcoinPriceUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(coins = result.data, isLoading = false)
                }
                is Resource.Error -> {
                    _state.value =
                        HomeState(error = result.message ?: Constants.AN_UNEXPECTED_ERROR_OCCURED)
                }
                is Resource.Loading -> {
                    _state.value = HomeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getInvestments(investOrder: InvestOrder) {
        getInvestmentsJob?.cancel()
        getInvestmentsJob = investmentUseCases.getInvestments(investOrder)
            .onEach { investments ->
                _state.value = state.value.copy(
                    investments = investments,
                    investOrder = investOrder
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.SaveInvestment -> {
                val investName = state.value.investName
                val investAmount = state.value.investAmount.toInt()
                val investHypothesis = state.value.investHypothesis
                val exchangeRate = state.value.coins?.get(0)?.toBitcoinPrice()?.price

//                if (investName.isBlank() || investAmount == 0 || investHypothesis.isBlank() ){
//                    return
//                }

                val investment = Investment(
                    name = investName, value = investAmount,
                    exchangeRate = exchangeRate!!,
                    hypothesis = investHypothesis,
                    dateOfCreation = Date().time, profit = 0.0
                )

                viewModelScope.launch {
                    investmentUseCases.addInvestment(investment)
                }

                _state.update {
                    it.copy(
                        isAddingInvestment = false,
                        investName = "",
                        investAmount = "",
                        investHypothesis = ""
                    )
                }
            }
            is HomeEvent.SetInvestName -> {
                _state.update {
                    it.copy(
                        investName = event.investName
                    )
                }
            }
            is HomeEvent.SetInvestAmount -> {
                _state.update {
                    it.copy(
                        investAmount = event.investAmount
                    )
                }
            }
            is HomeEvent.SetInvestHypothesis -> {
                _state.update {
                    it.copy(
                        investHypothesis = event.investHypothesis
                    )
                }
            }
            HomeEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingInvestment = true
                    )
                }
            }
            HomeEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingInvestment = false
                    )
                }
            }
            is HomeEvent.Order -> {
                if (state.value.investOrder::class == event.investOrder::class &&
                    state.value.investOrder.orderType == event.investOrder.orderType
                ) {
                    return
                }
                getInvestments(event.investOrder)
            }
            HomeEvent.RestoreInvest -> {
                TODO()
            }
            HomeEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }
}