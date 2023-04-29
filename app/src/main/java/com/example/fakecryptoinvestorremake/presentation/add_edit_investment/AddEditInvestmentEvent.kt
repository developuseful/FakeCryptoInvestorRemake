package com.example.fakecryptoinvestorremake.presentation.add_edit_investment

import androidx.compose.ui.focus.FocusState

sealed class AddEditInvestmentEvent{
    data class EnteredName(val value: String): AddEditInvestmentEvent()
    data class ChangeNameFocus(val focusState: FocusState): AddEditInvestmentEvent()
    data class EnteredHypothesis(val value: String): AddEditInvestmentEvent()
    data class ChangeHypothesisFocus(val focusState: FocusState): AddEditInvestmentEvent()
    data class EnteredValue(val value: String): AddEditInvestmentEvent()
    data class ChangeValueFocus(val focusState: FocusState): AddEditInvestmentEvent()
    object SaveInvestment: AddEditInvestmentEvent()
}

