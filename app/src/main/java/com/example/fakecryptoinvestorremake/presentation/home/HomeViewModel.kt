package com.example.fakecryptoinvestorremake.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.CoinDTO
import com.example.fakecryptoinvestorremake.data.remote.dto.toCoinPrice
import com.example.fakecryptoinvestorremake.domain.models.CoinType
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.use_case.ProfitUpdateUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price.GetCoinsUseCase
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
    private val getCoinsUseCase: GetCoinsUseCase,
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
        getCoinsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value =
                        state.value.copy(coins = result.data, isLoading = false, error = "")
                }
                is Resource.Error -> {
                    _state.value =
                        state.value.copy(
                            error = result.message ?: Constants.AN_UNEXPECTED_ERROR_OCCURED,
                            coins = null,
                            isLoading = false
                        )
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
            is HomeEvent.SaveInvestment -> {
                var coin: CoinDTO? = null

                if (state.value.coins != null){
                    coin = when(event.coinType){
                        CoinType.BTC -> {
                            state.value.coins!!.find { it.symbol == event.coinType.symbol }
                        }
                        CoinType.ETH -> {
                            state.value.coins!!.find { it.symbol == event.coinType.symbol }
                        }
                    }
                }

                val exchangeRate = coin?.toCoinPrice()?.price ?: return

                _state.value = state.value.copy(
                    isOrderSectionVisible = true
                )
                onEvent(HomeEvent.Order(InvestOrder.Id(OrderType.Descending)))

                val investment = Investment(
                    name = "",
                    hypothesis = "",
                    dateOfCreation = System.currentTimeMillis(),
                    //dateOfCreation = 0,
                    id = null,
                    exchangeRate = exchangeRate,
                    profit = 0.0,
                    value = 1000000,
                    purchaseCommission = 3,
                    salesCommission = 3,
                    exchangeRateVolatility = 0.0,
                    profitPercentage = -6.0,
                    symbol = event.coinType.symbol
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

            HomeEvent.UpdateCoinPrice -> {
                getBitcoinPrice()
            }

            HomeEvent.ProfitUpdate -> {
                profitUpdate()
            }
            HomeEvent.ToggleAddSection -> {
                _state.value = state.value.copy(
                    isAddSectionVisible = !state.value.isAddSectionVisible
                )
            }
        }
    }
}