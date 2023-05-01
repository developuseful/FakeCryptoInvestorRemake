package com.example.fakecryptoinvestorremake.presentation.add_edit_investment

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.theme.Background
import com.example.fakecryptoinvestorremake.theme.GreyDark2
import com.example.fakecryptoinvestorremake.theme.WhiteSoft

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditInvestmentScreen(
    navController: NavController,
    viewModel: AddEditInvestmentViewModel = hiltViewModel()
) {

    val nameState = viewModel.investName.value
    val hypothesisState = viewModel.investHypothesis.value
    val valueState = viewModel.investValue.value

//    val scaffoldState = rememberScaffoldState()
//
//    val scope = rememberCoroutineScope()
//
//    LaunchedEffect(key1 = true) {
//        viewModel.eventFlow.collectLatest { event ->
//            when (event) {
//                is AddEditInvestmentViewModel.UiEvent.ShowSnackbar -> {
//                    scaffoldState.snackbarHostState.showSnackbar(
//                        message = event.message
//                    )
//                }
//                is AddEditInvestmentViewModel.UiEvent.SaveInvest -> {
//                    navController.navigateUp()
//                }
//            }
//        }
//    }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    viewModel.onEvent(AddEditInvestmentEvent.SaveInvestment)
//                },
//                backgroundColor = MaterialTheme.colors.primary
//            ) {
//                Icon(imageVector = Icons.Default.Edit, contentDescription = "Save note")
//            }
//        },
//        scaffoldState = scaffoldState
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = nameState.text,
//                hint = nameState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddEditInvestmentEvent.EnteredName(it))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddEditInvestmentEvent.ChangeNameFocus(it))
//                },
//                isHintVisible = nameState.isHintVisible,
//                singleLine = true,
//                textStyle = MaterialTheme.typography.h5
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = hypothesisState.text,
//                hint = hypothesisState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddEditInvestmentEvent.EnteredHypothesis(it))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddEditInvestmentEvent.ChangeHypothesisFocus(it))
//                },
//                isHintVisible = hypothesisState.isHintVisible,
//                textStyle = MaterialTheme.typography.body1,
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TransparentHintTextField(
//                text = valueState.text,
//                hint = valueState.hint,
//                onValueChange = {
//                    viewModel.onEvent(AddEditInvestmentEvent.EnteredValue(it))
//                },
//                onFocusChange = {
//                    viewModel.onEvent(AddEditInvestmentEvent.ChangeValueFocus(it))
//                },
//                isHintVisible = valueState.isHintVisible,
//                textStyle = MaterialTheme.typography.body1
//            )
//
//        }
//    }


    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditInvestmentScreen.route)
                },
                backgroundColor = GreyDark2
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                    contentDescription = "Add note",
                    tint = WhiteSoft
                )
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
                    .fillMaxWidth()
                    .height(165.dp),
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
                }

            }

        }
    }
}


//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(state.value.investments) { investment ->
//                InvestListItem(
//                    invest = investment,
//                    onItemClick = {
//                        navController.navigate(
//                            Screen.AddEditInvestmentScreen.route +
//                                    "?investId=${investment.id}"
//                        )
//                    }
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }
