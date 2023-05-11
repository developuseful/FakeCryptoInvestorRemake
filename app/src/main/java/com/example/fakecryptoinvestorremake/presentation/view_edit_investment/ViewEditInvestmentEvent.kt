package com.example.fakecryptoinvestorremake.presentation.view_edit_investment

import androidx.compose.ui.focus.FocusState
import com.example.fakecryptoinvestorremake.domain.models.Investment

sealed class ViewEditInvestmentEvent{
    data class EnteredName(val value: String): ViewEditInvestmentEvent()
    data class ChangeNameFocus(val focusState: FocusState): ViewEditInvestmentEvent()
    data class EnteredHypothesis(val value: String): ViewEditInvestmentEvent()
    data class ChangeHypothesisFocus(val focusState: FocusState): ViewEditInvestmentEvent()
    data class EnteredValue(val value: String): ViewEditInvestmentEvent()
    data class ChangeValueFocus(val focusState: FocusState): ViewEditInvestmentEvent()


    data class EnteredPurchaseCommission(val value: String): ViewEditInvestmentEvent()
    data class EnteredSalesCommission(val value: String): ViewEditInvestmentEvent()

    object ToggleCommissionSection: ViewEditInvestmentEvent()

    object SaveInvestment: ViewEditInvestmentEvent()

    data class DeleteInvestment(val investment: Investment): ViewEditInvestmentEvent()

    object RestoreInvest: ViewEditInvestmentEvent()

    object GetCoinPrice: ViewEditInvestmentEvent()
}

