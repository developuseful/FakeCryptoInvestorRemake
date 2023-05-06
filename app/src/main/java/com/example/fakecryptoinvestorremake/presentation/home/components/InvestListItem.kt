package com.example.fakecryptoinvestorremake.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fakecryptoinvestorremake.domain.models.Investment
import com.example.fakecryptoinvestorremake.presentation.util.dividingNumberIntoDigitsDouble
import com.example.fakecryptoinvestorremake.presentation.util.dollarSignAtTheEnd
import com.example.fakecryptoinvestorremake.presentation.util.getTimePassed
import com.example.fakecryptoinvestorremake.theme.GreenSoft
import com.example.fakecryptoinvestorremake.theme.Grey666
import com.example.fakecryptoinvestorremake.theme.GreyLight
import com.example.fakecryptoinvestorremake.theme.RedSoft

@Composable
fun InvestListItem(
    invest: Investment,
    onItemClick: (Investment) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(horizontal = 10.dp)
            .border(
                width = 1.dp,
                color = GreyLight,
                shape = RoundedCornerShape(8.dp)
            ),
        backgroundColor = Color.White,

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(invest) },
        ) {
            val id = if (invest.id != null && invest.id < 10) "${invest.id}  " else invest.id.toString()
            Text(
                color = Grey666,
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(start = 28.dp, end = 22.dp),
                text = id,
                style = MaterialTheme.typography.subtitle1,
            )

            Divider(
                color = GreyLight,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 12.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .align(CenterVertically)
                    .weight(4f)
            ) {
                val investProfit = String.format("%.2f", invest.profit)
                val color = if (invest.profit >= 0) GreenSoft else RedSoft
                val investProfitFormatted = if (invest.profit >= 0) "+${investProfit}%" else "${investProfit}%"
                Text(
                    color = color,
                    text = investProfitFormatted,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                )
                val investmentRate = dividingNumberIntoDigitsDouble(invest.exchangeRate).dollarSignAtTheEnd()
                Text(
                    color = Grey666,
                    text = investmentRate,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                )
            }


            val timeHasPassed = getTimePassed(invest.dateOfCreation)
            val color = if (timeHasPassed == "now") GreenSoft else Grey666
            Text(
                color = color,
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(end = 16.dp),
                text = timeHasPassed,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }


}