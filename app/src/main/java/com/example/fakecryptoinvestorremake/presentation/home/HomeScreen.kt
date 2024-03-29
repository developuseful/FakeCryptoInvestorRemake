package com.example.fakecryptoinvestorremake.presentation.home


import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.presentation.creating_an_investment.CreatingAnInvestmentDialog
import com.example.fakecryptoinvestorremake.presentation.home.components.InvestListItem
import com.example.fakecryptoinvestorremake.presentation.home.components.OrderSection
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditInvestmentScreen.route)
                    //viewModel.onEvent(HomeEvent.ShowDialog)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        scaffoldState = scaffoldState
    ) {

        if (state.value.isAddingInvestment) {
            CreatingAnInvestmentDialog(state = state, onEvent = viewModel::onEvent)
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ваши инвестиции",
                    style = MaterialTheme.typography.h4
                )
                IconButton(
                    onClick = {
                        viewModel.onEvent(HomeEvent.ToggleOrderSection)
                        scope.launch { viewModel.profitUpdateUseCase() }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.value.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.value.investOrder,
                    onOrderChange = {
                        viewModel.onEvent(HomeEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (state.value.error.isNotBlank()) {
                Text(
                    text = state.value.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(CenterHorizontally)
                )
            }


            if (state.value.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(CenterHorizontally)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = "BTC " + state.value.coins?.get(0)
                            ?.toBitcoinPrice()?.price?.toInt().toString() + "$",
                        fontSize = 25.sp
                    )
                    /*
                    Text(
                        text = "ETH " + state.value.coins?.get(1)
                            ?.toBitcoinPrice()?.price?.toInt().toString() + "$",
                        fontSize = 25.sp
                    )
                     */
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            LazyColumnDemo(
                navController =navController,
                state = state
            )
        }
    }


}
/*
@Composable
fun LazyColumnDemo() {
    val list = listOf(
        "A", "B", "C", "D", "A", "B", "C", "D"
    ) + ((0..100).map { it.toString() })
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        items(items = list, itemContent = { item ->
            Log.d("COMPOSE", "This get rendered $item")
            when (item) {
                "A" -> {
                    Text(text = item, style = TextStyle(fontSize = 80.sp))
                }
                "B" -> {
                    Button(onClick = {}) {
                        Text(text = item, style = TextStyle(fontSize = 80.sp))
                    }
                }
                "C" -> {
                    Box(
                        modifier = Modifier
                            .background(Color.Gray)
                            .height(150.dp)
                            .fillMaxWidth()
                    )
                }
                "D" -> {
                    Text(text = item)
                }
                else -> {
                    Text(text = item, style = TextStyle(fontSize = 80.sp))
                }
            }
        })
    }
}

 */

@Composable
fun LazyColumnDemo(
    navController: NavController,
    state: State<HomeState>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.value.investments) { investment ->
            InvestListItem(
                invest = investment,
                onItemClick = {
                    navController.navigate(
                        Screen.AddEditInvestmentScreen.route +
                                "?investId=${investment.id}"
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}