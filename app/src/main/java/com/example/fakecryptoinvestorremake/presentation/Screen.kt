package com.example.fakecryptoinvestorremake.presentation

sealed class Screen(val route: String){
    object HomeScreen: Screen("home_screen")
    object AddEditInvestmentScreen: Screen("add_edit_investment_screen")

}
