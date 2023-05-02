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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.common.Resource
import com.example.fakecryptoinvestorremake.data.remote.dto.toBitcoinPrice
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder
import com.example.fakecryptoinvestorremake.domain.util.OrderType
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.presentation.home.components.InvestListItem
import com.example.fakecryptoinvestorremake.presentation.home.components.OrderSection
import com.example.fakecryptoinvestorremake.presentation.util.DividingNumberIntoDigits
import com.example.fakecryptoinvestorremake.theme.Background
import com.example.fakecryptoinvestorremake.theme.GreyDark2
import com.example.fakecryptoinvestorremake.theme.WhiteSoft
import kotlinx.coroutines.launch
import java.util.*

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
                    viewModel.onEvent(HomeEvent.SaveInvestment)
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
                    .fillMaxWidth(),
                backgroundColor = GreyDark2,
                elevation = 5.dp
            ) {
                Column() {
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

                            val bitcoinPrice = DividingNumberIntoDigits(
                                state.value.coins?.get(0)
                                    ?.toBitcoinPrice()?.price?.toInt()
                            )

                            val bitcoinPriceFormatted = if (bitcoinPrice == "null") "Refresh" else "BTC $bitcoinPrice $"

                            Text(
                                text = bitcoinPriceFormatted,
                                color = WhiteSoft,
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier
                                    .clickable { viewModel.onEvent(HomeEvent.UpdateBitcoinPrice) }
                            )
                        }


                        IconButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.ToggleOrderSection)
                                viewModel.onEvent(HomeEvent.ProfitUpdate)
                            },
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