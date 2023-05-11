package com.example.fakecryptoinvestorremake.presentation.home


import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.data.remote.dto.toCoinPrice
import com.example.fakecryptoinvestorremake.domain.models.CoinType
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.presentation.home.components.InvestListItem
import com.example.fakecryptoinvestorremake.presentation.home.components.OrderSection
import com.example.fakecryptoinvestorremake.presentation.util.dividingNumberIntoDigitsDouble
import com.example.fakecryptoinvestorremake.theme.Background
import com.example.fakecryptoinvestorremake.theme.GreyDark2
import com.example.fakecryptoinvestorremake.theme.WhiteSoft
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
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

            Column {
                AnimatedVisibility(
                    visible = state.value.isAddSectionVisible,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Column{
                        FloatingActionButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.SaveInvestment(CoinType.ETH))
                                viewModel.onEvent(HomeEvent.ToggleAddSection)
                            },
                            backgroundColor = GreyDark2
                        ) {
                            Text(text = CoinType.ETH.symbol, color = WhiteSoft)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        FloatingActionButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.SaveInvestment(CoinType.BTC))
                                viewModel.onEvent(HomeEvent.ToggleAddSection)
                            },
                            backgroundColor = GreyDark2
                        ) {
                            Text(text = CoinType.BTC.symbol, color = WhiteSoft)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(HomeEvent.ToggleAddSection)
                    },
                    backgroundColor = GreyDark2
                ) {
                    Icon(
                        imageVector = if (state.value.isAddSectionVisible)
                            ImageVector.vectorResource(R.drawable.baseline_remove_24)
                        else ImageVector.vectorResource(R.drawable.baseline_add_24),
                        contentDescription = "Add note",
                        tint = WhiteSoft
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
                elevation = 5.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .padding(top = 40.dp, bottom = 33.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (state.value.isLoading) {
                            CircularProgressIndicator(
                                color = WhiteSoft
                            )
                        } else {
                            val bitcoinPrice =
                                state.value.coins?.find { it.symbol == CoinType.BTC.symbol }
                                    ?.toCoinPrice()?.price?.let { double ->
                                        dividingNumberIntoDigitsDouble(
                                            double
                                        )
                                    }

                            val bitcoinPriceFormatted =
                                if (bitcoinPrice == "null") "Refresh" else "BTC  $bitcoinPrice $"

                            val ethereumPrice =
                                state.value.coins?.find { it.symbol == CoinType.ETH.symbol }
                                    ?.toCoinPrice()?.price?.let { double ->
                                        dividingNumberIntoDigitsDouble(
                                            double
                                        )
                                    }

                            val ethereumPriceFormatted =
                                if (ethereumPrice == "null") "Refresh" else "ETH  $ethereumPrice $"

                            Column(
                                modifier = Modifier
                                    .clickable { viewModel.onEvent(HomeEvent.UpdateCoinPrice) }
                            ) {
                                Text(
                                    text = bitcoinPriceFormatted,
                                    color = WhiteSoft,
                                    style = MaterialTheme.typography.h5
                                )
                                Text(
                                    text = ethereumPriceFormatted,
                                    color = WhiteSoft,
                                    style = MaterialTheme.typography.h5
                                )
                            }
                        }


                        IconButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.ToggleOrderSection)
                                viewModel.onEvent(HomeEvent.ProfitUpdate)
                            },
                            modifier = Modifier
                                .align(Bottom)
                        ) {
                            Icon(
                                imageVector = if (state.value.isOrderSectionVisible) ImageVector.vectorResource(
                                    R.drawable.arrow_up
                                )
                                else ImageVector.vectorResource(R.drawable.arrow_down),
                                contentDescription = "Sort",
                                tint = WhiteSoft
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
                                .background(GreyDark2)
                                .padding(start = 25.dp, end = 40.dp)
                                .height(55.dp),
                            noteOrder = state.value.investOrder,
                            onOrderChange = {
                                viewModel.onEvent(HomeEvent.Order(it))
                            }
                        )
                    }
                }
            }


            if (state.value.error.isNotBlank()) {
                Spacer(modifier = Modifier.height(27.dp))
                Text(
                    text = state.value.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 11.dp)
            ) {
                items(state.value.investments) { investment ->
                    InvestListItem(
                        invest = investment,
                        onItemClick = {
                            navController.navigate(
                                Screen.ViewEditInvestmentScreen.route +
                                        "?investId=${investment.id}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


        }
    }


}