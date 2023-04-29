package com.example.fakecryptoinvestorremake.presentation.add_edit_investment

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
import com.example.fakecryptoinvestorremake.domain.use_case.get_bitcoin_price.GetBitcoinPriceUseCase
import com.example.fakecryptoinvestorremake.domain.use_case.investment_use_cases.InvestmentUseCases
import com.example.fakecryptoinvestorremake.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditInvestmentViewModel @Inject constructor(
    private val investmentUseCases: InvestmentUseCases,
    private val getBitcoinPriceUseCase: GetBitcoinPriceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _investName = mutableStateOf(
        InvestmentTextFieldState(
            hint = "Название"
        )
    )
    val investName: State<InvestmentTextFieldState> = _investName

    private val _investHypothesis = mutableStateOf(
        InvestmentTextFieldState(
            hint = "Гипотеза"
        )
    )
    val investHypothesis: State<InvestmentTextFieldState> = _investHypothesis

    private val _investValue = mutableStateOf(
        InvestmentTextFieldState(
            hint = "Сумма"
        )
    )
    val investValue: State<InvestmentTextFieldState> = _investValue

    private val _addEditInvestmentState = mutableStateOf(AddEditInvestmentState())
    val addEditInvestmentState: State<AddEditInvestmentState> = _addEditInvestmentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentInvestId: Int? = null

    init {
        savedStateHandle.get<Int>("investId")?.let { investId ->
            if (investId != -1) {
                viewModelScope.launch {
                    investmentUseCases.getInvestment(investId)?.also { investment ->
                        currentInvestId = investment.id!!
                        _investName.value = investName.value.copy(
                            text = investment.name,
                            isHintVisible = false
                        )
                        _investHypothesis.value = investHypothesis.value.copy(
                            text = investment.hypothesis,
                            isHintVisible = false
                        )
                        _investValue.value = investValue.value.copy(
                            text = investment.value.toString(),
                            isHintVisible = false
                        )

                        _addEditInvestmentState.value = addEditInvestmentState.value.copy(
                            exchangeRate = investment.exchangeRate,
                            dateOfCreation = investment.dateOfCreation,
                            profit = investment.profit
                        )
                    }
                }
            } else {
                getBitcoinPrice()
            }
        }
    }

    fun onEvent(event: AddEditInvestmentEvent) {
        when (event) {
            is AddEditInvestmentEvent.EnteredName -> {
                _investName.value = investName.value.copy(
                    text = event.value
                )
            }
            is AddEditInvestmentEvent.ChangeNameFocus -> {
                _investName.value = investName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            investName.value.text.isBlank()
                )
            }
            is AddEditInvestmentEvent.EnteredHypothesis -> {
                _investHypothesis.value = _investHypothesis.value.copy(
                    text = event.value
                )
            }
            is AddEditInvestmentEvent.ChangeHypothesisFocus -> {
                _investHypothesis.value = _investHypothesis.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _investHypothesis.value.text.isBlank()
                )
            }

            is AddEditInvestmentEvent.SaveInvestment -> {

                viewModelScope.launch {
                    try {
                        //val dateOfCreation = if(currentInvestId == -1) System.currentTimeMillis() else addEditInvestmentState.value.dateOfCreation

                        investmentUseCases.addInvestment(
                            Investment(
                                name = investName.value.text,
                                hypothesis = investHypothesis.value.text,
                                dateOfCreation = addEditInvestmentState.value.dateOfCreation,
                                id = currentInvestId,
                                exchangeRate = addEditInvestmentState.value.exchangeRate,
                                profit = addEditInvestmentState.value.profit,
                                value = investValue.value.text.toInt()
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
            is AddEditInvestmentEvent.ChangeValueFocus -> {
                _investValue.value = _investValue.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _investValue.value.text.isBlank()
                )
            }
            is AddEditInvestmentEvent.EnteredValue -> {
                _investValue.value = _investValue.value.copy(
                    text = event.value
                )
            }
        }
    }

    private fun getBitcoinPrice() {
        getBitcoinPriceUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _addEditInvestmentState.value = addEditInvestmentState.value.copy(
                        exchangeRate = result.data?.get(0)?.toBitcoinPrice()?.price ?: 0.0,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _addEditInvestmentState.value =
                        AddEditInvestmentState(
                            error = result.message ?: Constants.AN_UNEXPECTED_ERROR_OCCURED
                        )
                }
                is Resource.Loading -> {
                    _addEditInvestmentState.value = AddEditInvestmentState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveInvest : UiEvent()
    }
}