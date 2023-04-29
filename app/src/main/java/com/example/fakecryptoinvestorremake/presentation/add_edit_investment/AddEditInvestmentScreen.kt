package com.example.fakecryptoinvestorremake.presentation.add_edit_investment

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.presentation.add_edit_investment.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditInvestmentScreen(
    navController: NavController,
    viewModel: AddEditInvestmentViewModel = hiltViewModel()
) {
    val nameState = viewModel.investName.value
    val hypothesisState = viewModel.investHypothesis.value
    val valueState = viewModel.investValue.value

    val scaffoldState = rememberScaffoldState()

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
}