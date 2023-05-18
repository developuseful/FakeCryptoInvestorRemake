package com.example.fakecryptoinvestorremake.presentation.view_edit_investment

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.presentation.util.*
import com.example.fakecryptoinvestorremake.presentation.view_edit_investment.components.TransparentHintTextField
import com.example.fakecryptoinvestorremake.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ViewEditInvestmentScreen(
    navController: NavController,
    viewModel: ViewEditInvestmentViewModel = hiltViewModel()
) {

    val nameState = viewModel.investName.value
    val valueState = viewModel.investValue.value
    val hypothesisState = viewModel.investHypothesis.value

    val viewEditInvestmentState = viewModel.viewEditInvestmentState.value
    val currentInvestment = viewEditInvestmentState.currentInvestment

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ViewEditInvestmentViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }
                is ViewEditInvestmentViewModel.UiEvent.SaveInvest -> {}
                ViewEditInvestmentViewModel.UiEvent.DeleteInvest -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            Column() {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(ViewEditInvestmentEvent.DeleteInvestment(investment = currentInvestment!!))
                        scope.launch {
                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                message = "Note deleted",
                                actionLabel = "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.onEvent(ViewEditInvestmentEvent.RestoreInvest)
                            }
                        }
                    },
                    backgroundColor = WhiteSoft
                ) {
                    Text(
                        text = "delete",
                        color = RedLight,
                        fontWeight = FontWeight.W300
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(ViewEditInvestmentEvent.SaveInvestment)
                    },
                    backgroundColor = WhiteSoft
                ) {
                    Text(
                        text = "save",
                        color = Grey666,
                        fontWeight = FontWeight.W300
                    )
                }
            }
        },
        scaffoldState = scaffoldState,
        backgroundColor = Background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 28.dp,
                    bottomEnd = 28.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = GreyDark2,
                elevation = 5.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .padding(top = 15.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_back_one_px),
                        contentDescription = "Back",
                        tint = WhiteSoft,
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(vertical = 24.dp)
                    )
                    Row{
                        Text(
                            text = currentInvestment?.symbol.toString(),
                            color = WhiteSoft,
                            style = MaterialTheme.typography.h5
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ID:",
                            color = WhiteSoft,
                            style = MaterialTheme.typography.h5
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentInvestment?.id.toString(),
                            color = WhiteSoft,
                            style = MaterialTheme.typography.h5
                        )
                    }

                    Spacer(modifier = Modifier.height(11.dp))
                    TransparentHintTextField(
                        text = nameState.text,
                        hint = nameState.hint,
                        onValueChange = {
                            viewModel.onEvent(ViewEditInvestmentEvent.EnteredName(it))
                        },
                        onFocusChange = {
                            viewModel.onEvent(ViewEditInvestmentEvent.ChangeNameFocus(it))
                        },
                        isHintVisible = nameState.isHintVisible,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = WhiteSoft,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            Spacer(modifier = Modifier.height(11.dp))
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(
                            width = 1.dp,
                            color = GreyLight,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    backgroundColor = androidx.compose.ui.graphics.Color.White,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 22.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "Investment amount:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        val maxChar = 15
                        val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
                        TransparentHintTextField(
                            text = valueState.text,
                            hint = valueState.hint,
                            onValueChange = {

                                if (it.isEmpty() || it.matches(pattern)) {
                                    if (it.length <= maxChar) {
                                        viewModel.onEvent(ViewEditInvestmentEvent.EnteredValue(it))
                                    }
                                }

                            },
                            onFocusChange = {
                                viewModel.onEvent(ViewEditInvestmentEvent.ChangeValueFocus(it))
                            },
                            isHintVisible = valueState.isHintVisible,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Grey666,
                                textDecoration = TextDecoration.Underline
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            visualTransformation = ThousandSeparatorTransformation()
                        )
                        Spacer(modifier = Modifier.height(28.dp))


                        // Commission
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = GreyLight,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .padding(start = 16.dp, end = 28.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Commission:",
                                        color = GreyDark2,
                                        style = MaterialTheme.typography.h6
                                    )
                                    Icon(
                                        imageVector = if (viewEditInvestmentState.isCommissionSectionVisible)
                                            ImageVector.vectorResource(R.drawable.arrow_up)
                                        else ImageVector.vectorResource(R.drawable.arrow_down),
                                        contentDescription = "Expand block",
                                        tint = Grey666,
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .clickable { viewModel.onEvent(ViewEditInvestmentEvent.ToggleCommissionSection) }
                                    )
                                }

                                AnimatedVisibility(
                                    visible = viewEditInvestmentState.isCommissionSectionVisible,
                                    enter = fadeIn() + slideInVertically(),
                                    exit = fadeOut() + slideOutVertically()
                                ) {

                                    Column() {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Column(
                                            modifier = Modifier
                                                .padding(vertical = 16.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Purchase commission:",
                                                    color = GreyDark2,
                                                    style = MaterialTheme.typography.subtitle1
                                                )
                                                Row(
                                                    modifier = Modifier.align(alignment = Bottom)
                                                ) {
                                                    val maxCharPurchaseCommission = 3
                                                    val widthPurchaseCommission = sizeTextField(viewEditInvestmentState.purchaseCommission.length)
                                                    val focusManager = LocalFocusManager.current
                                                    BasicTextField(
                                                        value = viewEditInvestmentState.purchaseCommission,
                                                        onValueChange = {
                                                            if (it.length <= maxCharPurchaseCommission) {
                                                                viewModel.onEvent(
                                                                    ViewEditInvestmentEvent.EnteredPurchaseCommission(
                                                                        it
                                                                    )
                                                                )
                                                            }
                                                        },
                                                        modifier = Modifier
                                                            .width(widthPurchaseCommission),
                                                        textStyle = TextStyle(
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Normal,
                                                            color = Grey666,
                                                            textDecoration = TextDecoration.Underline
                                                        ),
                                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                                                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                                                    )
                                                    Text(
                                                        text = "%",
                                                        color = Grey666,
                                                        style = MaterialTheme.typography.subtitle1,
                                                        modifier = Modifier.align(alignment = Bottom)
                                                    )
                                                }
                                            }

                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Sales commission:",
                                                color = GreyDark2,
                                                style = MaterialTheme.typography.subtitle1
                                            )
                                            Row(
                                                modifier = Modifier.align(alignment = Bottom)
                                            ) {
                                                val maxCharSalesCommission = 3
                                                val widthSalesCommission = sizeTextField(viewEditInvestmentState.salesCommission.length)
                                                val focusManager = LocalFocusManager.current
                                                BasicTextField(
                                                    value = viewEditInvestmentState.salesCommission,
                                                    onValueChange = {
                                                        if (it.length <= maxCharSalesCommission) {
                                                            viewModel.onEvent(
                                                                ViewEditInvestmentEvent.EnteredSalesCommission(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .width(widthSalesCommission),
                                                    textStyle = TextStyle(
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Normal,
                                                        color = Grey666,
                                                        textDecoration = TextDecoration.Underline
                                                    ),
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                                                )
                                                Text(
                                                    text = "%",
                                                    color = Grey666,
                                                    style = MaterialTheme.typography.subtitle1,
                                                    modifier = Modifier.align(alignment = Bottom)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Profit:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        var investProfit = ""
                        var color = Grey666
                        var investProfitFormatted = ""

                        var profitValue = ""
                        var profitValueFormatted = "0"

                        if (currentInvestment?.profitPercentage != null) {
                            investProfit = String.format("%.2f", currentInvestment.profitPercentage)
                            color =
                                if (currentInvestment.profitPercentage >= 0) GreenSoft else RedSoft
                            investProfitFormatted =
                                if (currentInvestment.profitPercentage >= 0) "+${investProfit}%" else "${investProfit}%"

                            if (valueState.text != "") {
                                profitValue =
                                    dividingNumberIntoDigits((valueState.text.toDouble() * currentInvestment.profitPercentage / 100))
                                profitValueFormatted =
                                    if (currentInvestment.profitPercentage >= 0) "+${profitValue}" else profitValue
                            }
                        }

                        Row() {
                            Text(text = profitValueFormatted, color = color)
                            Spacer(modifier = Modifier.width(24.dp))
                            Text(text = investProfitFormatted, color = color)
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
                Spacer(modifier = Modifier.height(11.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(
                            width = 1.dp,
                            color = GreyLight,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    backgroundColor = androidx.compose.ui.graphics.Color.White
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 22.dp)
                    ) {
                        Text(
                            text = "Investment rate:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        var investmentRate = "No data"
                        if (currentInvestment?.exchangeRate != null) {
                            investmentRate =
                                dividingNumberIntoDigits(currentInvestment.exchangeRate).dollarSignAtTheEnd()
                        }
                        Text(text = investmentRate, color = Grey666)

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Current exchange rate:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        var color = Grey666
                        var currentExchangeRate = ""

                        if (viewEditInvestmentState.error.isBlank()) {
                            currentExchangeRate =
                                dividingNumberIntoDigits(viewEditInvestmentState.currentExchangeRate).dollarSignAtTheEnd()
                            if (currentInvestment?.exchangeRateVolatility != null) {
                                color =
                                    if (currentInvestment.exchangeRateVolatility >= 0) GreenSoft else RedSoft
                            }
                        } else {
                            currentExchangeRate = viewEditInvestmentState.error
                            color = RedSoft
                        }

                        var exchangeRateVolatility = ""
                        var exchangeRateVolatilityFormatted = ""
                        if (currentInvestment?.exchangeRateVolatility != null) {
                            exchangeRateVolatility =
                                String.format("%.2f", currentInvestment.exchangeRateVolatility)
                            exchangeRateVolatilityFormatted =
                                if (currentInvestment.exchangeRateVolatility >= 0) "+${exchangeRateVolatility}%" else "${exchangeRateVolatility}%"
                        }
                        Row() {
                            Text(
                                text = currentExchangeRate,
                                color = color,
                                modifier = Modifier
                                    .clickable { viewModel.onEvent(ViewEditInvestmentEvent.GetCoinPrice) }
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            Text(text = exchangeRateVolatilityFormatted, color = color)
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
                Spacer(modifier = Modifier.height(11.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(
                            width = 1.dp,
                            color = GreyLight,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    backgroundColor = androidx.compose.ui.graphics.Color.White
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 22.dp)
                    ) {
                        Text(
                            text = "Attachment date:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        val dateOfCreation = if (currentInvestment?.dateOfCreation != null) {
                            getDateFormatted(currentInvestment.dateOfCreation)
                        } else {
                            "No data"
                        }

                        Text(text = dateOfCreation!!, color = Grey666)

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Time has passed:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        var timePassed = "No data"
                        if (currentInvestment?.dateOfCreation != null) {
                            timePassed = getTimePassed(currentInvestment.dateOfCreation)
                        }
                        Text(text = timePassed, color = Grey666)

                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
                Spacer(modifier = Modifier.height(11.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(
                            width = 1.dp,
                            color = GreyLight,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    backgroundColor = androidx.compose.ui.graphics.Color.White
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 22.dp, end = 32.dp)
                    ) {
                        Text(
                            text = "Hypothesis:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))
                        TransparentHintTextField(
                            text = hypothesisState.text,
                            hint = hypothesisState.hint,
                            onValueChange = {
                                viewModel.onEvent(ViewEditInvestmentEvent.EnteredHypothesis(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(ViewEditInvestmentEvent.ChangeHypothesisFocus(it))
                            },
                            isHintVisible = hypothesisState.isHintVisible,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Grey666,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
                Spacer(modifier = Modifier.height(300.dp))
            }

        }
    }
}

fun sizeTextField(length: Int): Dp {
    return when (length) {
        2 -> 25.dp
        3 -> 35.dp
        else -> 15.dp
    }
}

