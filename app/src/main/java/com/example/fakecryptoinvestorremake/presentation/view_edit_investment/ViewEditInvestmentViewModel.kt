package com.example.fakecryptoinvestorremake.presentation.view_edit_investment

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakecryptoinvestorremake.common.Constants
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.models.InvalidInvestmentException
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.domain.use_case.ProfitUpdateUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price.GetBitcoinPriceUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.InvestmentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewEditInvestmentViewModel @Inject constructor(
    private val investmentUseCases: InvestmentUseCases,
    private val getBitcoinPriceUseCase: GetBitcoinPriceUseCase,
    private val profitUpdateUseCase: ProfitUpdateUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _investName = mutableStateOf(
        InvestmentTextFieldState(
            hint = "Enter the title"
        )
    )
    val investName: State<InvestmentTextFieldState> = _investName

    private val _investHypothesis = mutableStateOf(
        InvestmentTextFieldState(
            hint = "For example:\nI expect it to double in two months."
        )
    )
    val investHypothesis: State<InvestmentTextFieldState> = _investHypothesis

    private val _investValue = mutableStateOf(
        InvestmentTextFieldState(
            hint = "Enter value"
        )
    )
    val investValue: State<InvestmentTextFieldState> = _investValue

    private val _viewEditInvestmentState = mutableStateOf(ViewEditInvestmentState())
    val viewEditInvestmentState: State<ViewEditInvestmentState> = _viewEditInvestmentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentInvestId: Int? = null

    private var recentlyDeletedInvestment: Investment? = null

    init {
        savedStateHandle.get<Int>("investId")?.let { investId ->
            viewModelScope.launch {
                investmentUseCases.getInvestment(investId)?.also { investment ->
                    currentInvestId = investment.id!!

                    val isHintVisibleForName = if (investment.name == "") true else false
                    _investName.value = investName.value.copy(
                        text = investment.name,
                        isHintVisible = isHintVisibleForName
                    )

                    _investValue.value = investValue.value.copy(
                        text = investment.value.toString(),
                        isHintVisible = false
                    )

                    val isHintVisibleForHypothesis = if (investment.hypothesis == "") true else false
                    _investHypothesis.value = investHypothesis.value.copy(
                        text = investment.hypothesis,
                        isHintVisible = isHintVisibleForHypothesis
                    )

                    _viewEditInvestmentState.value = viewEditInvestmentState.value.copy(
                        currentInvestment = investment
                    )
                }
            }
        }
        getBitcoinPrice()
    }

    fun onEvent(event: ViewEditInvestmentEvent) {
        when (event) {
            is ViewEditInvestmentEvent.EnteredName -> {
                _investName.value = investName.value.copy(
                    text = event.value
                )
            }
            is ViewEditInvestmentEvent.ChangeNameFocus -> {
                _investName.value = investName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            investName.value.text.isBlank()
                )
            }
            is ViewEditInvestmentEvent.EnteredHypothesis -> {
                _investHypothesis.value = _investHypothesis.value.copy(
                    text = event.value
                )
            }
            is ViewEditInvestmentEvent.ChangeHypothesisFocus -> {
                _investHypothesis.value = _investHypothesis.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _investHypothesis.value.text.isBlank()
                )
            }

            is ViewEditInvestmentEvent.SaveInvestment -> {
                viewModelScope.launch {
                    try {
                        viewEditInvestmentState.value.currentInvestment?.copy(
                            name = investName.value.text,
                            value = investValue.value.text.toInt(),
                            hypothesis = investHypothesis.value.text
                        )?.let { investment ->
                            investmentUseCases.addInvestment(
                                investment = investment
                            )
                        }
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = "Investment saved"
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveInvest)
                    } catch (e: InvalidInvestmentException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }


            }
            is ViewEditInvestmentEvent.ChangeValueFocus -> {
                _investValue.value = _investValue.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _investValue.value.text.isBlank()
                )
            }
            is ViewEditInvestmentEvent.EnteredValue -> {
                _investValue.value = _investValue.value.copy(
                    text = event.value
                )
            }
            is ViewEditInvestmentEvent.DeleteInvestment -> {
                viewModelScope.launch {
                    try {
                        investmentUseCases.deleteInvestment(event.investment)
                        recentlyDeletedInvestment = event.investment
                        _eventFlow.emit(UiEvent.DeleteInvest)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't delete investment"
                            )
                        )
                    }
                }
            }
            ViewEditInvestmentEvent.RestoreInvest -> {
                viewModelScope.launch {
                    investmentUseCases.addInvestment(recentlyDeletedInvestment ?: return@launch)
                    recentlyDeletedInvestment = null
                }
            }
            ViewEditInvestmentEvent.GetBitcoinPrice -> {
                getBitcoinPrice()
            }
        }
    }

    private fun getBitcoinPrice() {
        getBitcoinPriceUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    profitUpdateUseCase.invoke()
                    if(currentInvestId != null){
                        investmentUseCases.getInvestment(currentInvestId!!)?.also { investment ->
                            _viewEditInvestmentState.value = viewEditInvestmentState.value.copy(
                                currentInvestment = investment,
                                currentExchangeRate = result.data?.get(0)?.toBitcoinPrice()?.price ?: 0.0,
                                isLoading = false,
                                error = ""
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _viewEditInvestmentState.value =
                        viewEditInvestmentState.value.copy(
                            error = result.message ?: Constants.AN_UNEXPECTED_ERROR_OCCURED
                        )
                }
                is Resource.Loading -> {
                    _viewEditInvestmentState.value = viewEditInvestmentState.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveInvest : UiEvent()
        object DeleteInvest : UiEvent()
    }
}