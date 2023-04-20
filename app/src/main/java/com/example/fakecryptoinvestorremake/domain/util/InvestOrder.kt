package com.example.fakecryptoinvestorremake.domain.util

sealed class InvestOrder(val orderType: OrderType) {
    class Profit(orderType: OrderType): InvestOrder(orderType)
    class Date(orderType: OrderType): InvestOrder(orderType)

    fun copy(orderType: OrderType): InvestOrder {
        return when(this) {
            is Profit -> Profit(orderType)
            is Date -> Date(orderType)
        }
    }
}
