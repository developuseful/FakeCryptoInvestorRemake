package com.example.fakecryptoinvestorremake.presentation.home


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.data.remote.dto.toCoinPrice
import com.example.fakecryptoinvestorremake.domain.models.CoinType
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.presentation.home.components.InvestListItem
import com.example.fakecryptoinvestorremake.presentation.home.components.OrderSection
import com.example.fakecryptoinvestorremake.presentation.home.components.SwipeBackground
import com.example.fakecryptoinvestorremake.presentation.util.dividingNumberIntoDigits
import com.example.fakecryptoinvestorremake.theme.Background
import com.example.fakecryptoinvestorremake.theme.GreyDark2
import com.example.fakecryptoinvestorremake.theme.WhiteSoft
import kotlinx.coroutines.launch
import java.util.*
import kotlinx.coroutines.flow.collectLatest as collectLatest1


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refresh() })

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest1 { event ->
            when (event) {
                is HomeViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }
            }
        }
    }


    Scaffold(
        floatingActionButton = {
            Column {
                AnimatedVisibility(
                    visible = state.value.isAddSectionVisible,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Column {
                        FloatingActionButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.SaveInvestment(CoinType.ETH))
                            },
                            backgroundColor = GreyDark2
                        ) {
                            Text(text = CoinType.ETH.symbol, color = WhiteSoft)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        FloatingActionButton(
                            onClick = {
                                viewModel.onEvent(HomeEvent.SaveInvestment(CoinType.BTC))
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
        backgroundColor = Background,
        modifier = Modifier
            .pullRefresh(pullRefreshState)
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
                Column{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .padding(top = 40.dp, bottom = 33.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (state.value.coins != null) {
                            val bitcoinPrice =
                                state.value.coins?.find { it.symbol == CoinType.BTC.symbol }
                                    ?.toCoinPrice()?.price?.let { double ->
                                        dividingNumberIntoDigits(
                                            double
                                        )
                                    }

                            val bitcoinPriceFormatted = "BTC  $bitcoinPrice $"

                            val ethereumPrice =
                                state.value.coins?.find { it.symbol == CoinType.ETH.symbol }
                                    ?.toCoinPrice()?.price?.let { double ->
                                        dividingNumberIntoDigits(
                                            double
                                        )
                                    }

                            val ethereumPriceFormatted = "ETH  $ethereumPrice $"

                            Column(
                                modifier = Modifier
                                    .clickable { viewModel.refresh() }
                            ) {
                                Text(
                                    text = bitcoinPriceFormatted,
                                    color = WhiteSoft,
                                    style = MaterialTheme.typography.h5
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = ethereumPriceFormatted,
                                    color = WhiteSoft,
                                    style = MaterialTheme.typography.h5
                                )
                            }
                        } else {
                            Text(
                                text = "Click to update",
                                color = WhiteSoft,
                                style = MaterialTheme.typography.h5,
                                modifier = Modifier
                                    .clickable { viewModel.refresh() },
                                textDecoration = TextDecoration.Underline
                            )
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

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.value.error.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        Text(
                            text = state.value.error,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier
                                .padding(28.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                } else {
                    if (viewModel.scrollState) {
                        scope.launch { lazyListState.scrollToItem(0) }
                    }
                    viewModel.scrollState = true

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = lazyListState,
                        contentPadding = PaddingValues(top = 7.dp, bottom = 100.dp),
                    ) {
                        items(
                            items = state.value.investments,
                            key = { investmentKey -> investmentKey.id!! },
                            itemContent = { investmentItem ->
                                val currentItem by rememberUpdatedState(investmentItem)
                                val dismissState = rememberDismissState(
                                    confirmStateChange = {
                                        if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                                            viewModel.onEvent(HomeEvent.DeleteInvestment(currentItem))
                                            scope.launch {
                                                val result =
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        message = "Investment deleted",
                                                        actionLabel = "Undo"
                                                    )
                                                if (result == SnackbarResult.ActionPerformed) {
                                                    viewModel.onEvent(HomeEvent.RestoreNote)
                                                }
                                            }
                                            true
                                        } else false
                                    }
                                )

                                if (dismissState.isDismissed(DismissDirection.EndToStart) ||
                                    dismissState.isDismissed(DismissDirection.StartToEnd)
                                ) {
                                    viewModel.onEvent(HomeEvent.DeleteInvestment(investmentItem))
                                    scope.launch {
                                        val result = scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Investment deleted",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.onEvent(HomeEvent.RestoreNote)
                                        }
                                    }
                                }

                                SwipeToDismiss(
                                    state = dismissState,
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .animateItemPlacement(),
                                    directions = setOf(
                                        DismissDirection.StartToEnd,
                                        DismissDirection.EndToStart
                                    ),
                                    dismissThresholds = { direction ->
                                        FractionalThreshold(
                                            if (direction == DismissDirection.StartToEnd) 0.66f else 0.50f
                                        )
                                    },
                                    background = {
                                        SwipeBackground(dismissState)
                                    },
                                    dismissContent = {
                                        InvestListItem(
                                            invest = investmentItem,
                                            onItemClick = {
                                                navController.navigate(
                                                    Screen.ViewEditInvestmentScreen.route +
                                                            "?investId=${investmentItem.id}"
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }

                }

            }

        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = TopCenter
        ) {
            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState
            )
        }
    }
}