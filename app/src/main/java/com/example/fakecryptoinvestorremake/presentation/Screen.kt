package com.example.fakecryptoinvestorremake.presentation

sealed class Screen(val route: String){
    object HomeScreen: Screen("home_screen")
    object ViewEditInvestmentScreen: Screen("view_edit_investment_screen")

}
