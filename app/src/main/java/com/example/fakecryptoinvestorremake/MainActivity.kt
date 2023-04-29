package com.example.fakecryptoinvestorremake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakecryptoinvestorremake.presentation.Screen
import com.example.fakecryptoinvestorremake.presentation.add_edit_investment.AddEditInvestmentScreen
import com.example.fakecryptoinvestorremake.presentation.home.HomeScreen
import com.example.fakecryptoinvestorremake.ui.theme.FakeCryptoInvestorRemakeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeCryptoInvestorRemakeTheme() {
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
                            route = Screen.AddEditInvestmentScreen.route +
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
                            AddEditInvestmentScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}