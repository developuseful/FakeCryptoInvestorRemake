package com.example.fakecryptoinvestorremake.presentation.util

import java.util.*

fun DividingNumberIntoDigits(number: Int?): String{
    return String.format(
        Locale.CANADA_FRENCH, "%,d", number)
}

fun DividingNumberIntoDigitsDouble(number: Double): String{
    val string = String.format(
        Locale.CANADA_FRENCH, "%,f", number)
    return string.substring(0, string.indexOf(","))
}