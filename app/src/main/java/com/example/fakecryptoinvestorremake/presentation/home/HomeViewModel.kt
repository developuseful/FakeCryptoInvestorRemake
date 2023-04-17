package com.example.fakecryptoinvestorremake.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price.GetBitcoinPriceUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.InvestmentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBitcoinPriceUseCase: GetBitcoinPriceUseCase,
    private val investmentUseCases: InvestmentUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state : StateFlow<HomeState> = _state

    private var getInvestmentsJob: Job? = null

    init {
        getBitcoinPrice()
        getInvestments()
    }

    private fun getBitcoinPrice(){
            getBitcoinPriceUseCase().onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = HomeState(bitcoinPrice = result.data)
                    }
                    is Resource.Error -> {
                        _state.value = HomeState(error = result.message ?: Constants.AN_UNEXPECTED_ERROR_OCCURED)
                    }
                    is Resource.Loading -> {
                        _state.value = HomeState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getInvestments() {
        getInvestmentsJob?.cancel()
        getInvestmentsJob = investmentUseCases.getInvestments()
            .onEach { investments ->
                _state.value = state.value.copy(
                    investments = investments
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent){
        when(event){
            HomeEvent.SaveInvestment -> {
                val investName = state.value.investName
                val investAmount = state.value.investAmount
                val investHypothesis = state.value.investHypothesis

//                if (investName.isBlank() || investAmount == 0 || investHypothesis.isBlank() ){
//                    return
//                }

                val investment = Investment(
                    name = investName, value = investAmount,
                    hypothesis = investHypothesis,
                    dateOfCreation = 10000
                )

                viewModelScope.launch {
                    investmentUseCases.addInvestment.invoke(investment)
                }

                _state.update { it.copy(
                    isAddingInvestment = false,
                    investName = "",
                    investAmount = 0,
                    investHypothesis = ""
                ) }
            }
            is HomeEvent.SetInvestName -> {
                _state.update { it.copy(
                    investName = event.investName
                ) }
            }
            is HomeEvent.SetInvestAmount -> {
                _state.update { it.copy(
                    investAmount = event.investAmount
                ) }
            }
            is HomeEvent.SetInvestHypothesis -> {
                _state.update { it.copy(
                    investHypothesis = event.investHypothesis
                ) }
            }
            HomeEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingInvestment = true
                ) }
            }
            HomeEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingInvestment = false
                ) }
            }

        }
    }
}