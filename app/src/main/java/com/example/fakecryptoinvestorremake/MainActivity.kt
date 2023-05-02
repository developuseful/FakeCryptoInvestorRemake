package com.example.fakecryptoinvestorremake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.presentation.view_edit_investment.ViewEditInvestmentScreen
import com.example.fakecryptoinvestorremake.presentation.home.HomeScreen
import com.example.fakecryptoinvestorremake.theme.FakeCryptoInvestorRemakeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeCryptoInvestorRemakeTheme(
            ) {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(
                            route = Screen.HomeScreen.route
                        ) {
                            HomeScreen(navController)
                        }
                        composable(
                            route = Screen.ViewEditInvestmentScreen.route +
                                    "?investId={investId}",
                            arguments = listOf(
                                navArgument(
                                    name = "investId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            ViewEditInvestmentScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}