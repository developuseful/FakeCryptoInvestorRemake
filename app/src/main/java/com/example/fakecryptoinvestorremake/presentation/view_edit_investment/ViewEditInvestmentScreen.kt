package com.example.fakecryptoinvestorremake.presentation.view_edit_investment

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.presentation.util.DividingNumberIntoDigits
import com.example.fakecryptoinvestorremake.presentation.util.DividingNumberIntoDigitsDouble
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

    val currentInvestment = viewModel.viewEditInvestmentState.value.investment


/*    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditInvestmentViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditInvestmentViewModel.UiEvent.SaveInvest -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditInvestmentEvent.SaveInvestment)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = nameState.text,
                hint = nameState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditInvestmentEvent.EnteredName(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditInvestmentEvent.ChangeNameFocus(it))
                },
                isHintVisible = nameState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = hypothesisState.text,
                hint = hypothesisState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditInvestmentEvent.EnteredHypothesis(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditInvestmentEvent.ChangeHypothesisFocus(it))
                },
                isHintVisible = hypothesisState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = valueState.text,
                hint = valueState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditInvestmentEvent.EnteredValue(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditInvestmentEvent.ChangeValueFocus(it))
                },
                isHintVisible = valueState.isHintVisible,
                textStyle = MaterialTheme.typography.body1
            )

        }
    }

 */


    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ViewEditInvestmentViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_back_one_px),
                            contentDescription = "Back",
                            tint = WhiteSoft
                        )
                    }
                    Text(
                        text = "Detailing",
                        color = WhiteSoft,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            color = WhiteSoft,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            Spacer(modifier = Modifier.height(11.dp))
            Column() {
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
                            .padding(start = 16.dp, top = 22.dp)
                    ) {
                        Text(
                            text = "Investment amount:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                            )
                        Spacer(modifier = Modifier.height(11.dp))
                        val maxChar = 25
                        TransparentHintTextField(
                            text = valueState.text,
                            hint = valueState.hint,
                            onValueChange = {
                                if(it.length <= maxChar){
                                    viewModel.onEvent(ViewEditInvestmentEvent.EnteredValue(it))
                                }
                            },
                            onFocusChange = {
                                viewModel.onEvent(ViewEditInvestmentEvent.ChangeValueFocus(it))
                            },
                            isHintVisible = valueState.isHintVisible,
                            textStyle  = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Grey666,
                                textDecoration = TextDecoration.Underline
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            text = "Profit:",
                            color = GreyDark2,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(11.dp))

                        var investProfit = ""
                        var color = Grey666
                        var investProfitFormatted = ""

                        var profitValue = ""
                        var profitValueFormatted = "0"

                        if (currentInvestment?.profit != null){
                            investProfit = String.format("%.2f", currentInvestment.profit)
                            color = if (currentInvestment.profit >= 0) GreenSoft else RedSoft
                            investProfitFormatted = if (currentInvestment.profit >= 0) "+${investProfit}%" else "${investProfit}%"

                            if (valueState.text != ""){
                                profitValue = DividingNumberIntoDigitsDouble((valueState.text.toDouble() * currentInvestment.profit / 100))
                                profitValueFormatted = if (currentInvestment.profit >= 0) "+${profitValue}" else profitValue
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
            }

        }
    }
}
