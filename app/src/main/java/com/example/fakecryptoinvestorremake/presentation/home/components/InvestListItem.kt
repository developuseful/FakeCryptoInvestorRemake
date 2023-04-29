package com.example.fakecryptoinvestorremake.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.presentation.util.getTimePassed

@Composable
fun InvestListItem(
    invest: Investment,
    onItemClick: (Investment) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(invest) },
            //.padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Column() {
            Text(
                text = invest.name,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
            Row() {
                Text(
                    text = invest.value.toString(),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis
                )

                val investProfit = String.format("%.2f", invest.profit)
                Text(
                    color = if (invest.profit >= 0) Color.Green else Color.Red,
                    text = if (invest.profit >= 0) "+${investProfit}" else investProfit,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        val timeHasPassed = getTimePassed(invest.dateOfCreation)
        Text(
            modifier = Modifier.align(CenterVertically),
            text = timeHasPassed!!,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis
        )



    }


}