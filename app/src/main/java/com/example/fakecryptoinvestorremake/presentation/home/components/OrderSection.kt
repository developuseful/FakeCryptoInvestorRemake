package com.example.fakecryptoinvestorremake.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.fakecryptoinvestorremake.R
import com.example.fakecryptoinvestorremake.domain.util.InvestOrder
import com.example.fakecryptoinvestorremake.domain.util.OrderType
import com.example.fakecryptoinvestorremake.theme.GreenSoft
import com.example.fakecryptoinvestorremake.theme.Grey666
import com.example.fakecryptoinvestorremake.theme.GreyDark
import com.example.fakecryptoinvestorremake.theme.WhiteSoft

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: InvestOrder = InvestOrder.Profit(OrderType.Descending),
    onOrderChange: (InvestOrder) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "ID",
            style = MaterialTheme.typography.subtitle1,
            color = if (noteOrder is InvestOrder.Id) GreenSoft else Grey666,
            modifier = Modifier.clickable { onOrderChange(InvestOrder.Id(noteOrder.orderType)) }
        )
        Text(
            text = "Profit",
            style = MaterialTheme.typography.subtitle1,
            color = if (noteOrder is InvestOrder.Profit) GreenSoft else Grey666,
            modifier = Modifier.clickable { onOrderChange(InvestOrder.Profit(noteOrder.orderType)) }
        )

        Text(
            text = "Date of creation",
            style = MaterialTheme.typography.subtitle1,
            color = if (noteOrder is InvestOrder.Date) GreenSoft else Grey666,
            modifier = Modifier.clickable { onOrderChange(InvestOrder.Date(noteOrder.orderType)) }
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.arrow_up_one_px),
            modifier = Modifier.clickable { onOrderChange(noteOrder.copy(OrderType.Ascending)) },
            contentDescription = "Ascending",
            tint = if (noteOrder.orderType is OrderType.Ascending) GreenSoft else Grey666
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.arrow_down_one_px),
            modifier = Modifier.clickable { onOrderChange(noteOrder.copy(OrderType.Descending)) },
            contentDescription = "Descending",
            tint = if (noteOrder.orderType is OrderType.Descending) GreenSoft else Grey666
        )
    }


}
