package com.example.fakecryptoinvestorremake.presentation.creating_an_investment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fakecryptoinvestorremake.presentation.home.HomeEvent
import com.example.fakecryptoinvestorremake.presentation.home.HomeState

@Composable
fun CreatingAnInvestmentDialog(
    state: State<HomeState>,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(HomeEvent.HideDialog)
        },
        title = { Text(text = "Создание инвестиции") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = state.value.investName,
                    onValueChange = {
                        onEvent(HomeEvent.SetInvestName(it))
                    },
                    placeholder = {
                        Text(text = "First name")
                    }
                )
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        onEvent(HomeEvent.SaveInvestment)
                    }) {
                    Text(text = "Создать")
                }
            }
        }
    )

}