package com.example.fakecryptoinvestorremake.presentation.home

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
        getBitcoinPrice()
        getInvestments(InvestOrder.Profit(OrderType.Descending))
    }


    private fun profitUpdate() {
        viewModelScope.launch { profitUpdateUseCase.invoke() }
    }

    private fun getBitcoinPrice() {
        getBitcoinPriceUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(coins = result.data, isLoading = false, error = "")
                }
                is Resource.Error -> {
                    _state.value =
                        state.value.copy(error = result.message ?: Constants.AN_UNEXPECTED_ERROR_OCCURED, coins = null, isLoading = false)
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(isLoading = true, coins = null)
                }
            }
        }.launchIn(viewModelScope)
        profitUpdate()
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
                val exchangeRate = state.value.coins?.get(0)?.toBitcoinPrice()?.price ?: return

                _state.value = state.value.copy(
                    isOrderSectionVisible = true
                )
                onEvent(HomeEvent.Order(InvestOrder.Id(OrderType.Descending)))

                val investment = Investment(
                        name = "",
                        hypothesis = "",
                        dateOfCreation = System.currentTimeMillis(),
                        id = null,
                        exchangeRate = exchangeRate,
                        profit = 0.0,
                        value = 1000000
                    )

                viewModelScope.launch {
                    investmentUseCases.addInvestment(investment)
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
            HomeEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            HomeEvent.UpdateBitcoinPrice -> {
                getBitcoinPrice()
            }

            HomeEvent.ProfitUpdate -> {
                profitUpdate()
            }
        }
    }
}