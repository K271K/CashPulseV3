package com.example.cashpulse.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.cashpulse.MainActivityViewModel
import com.example.cashpulse.MainActivityViewModelFactory
import core.navigation.SubGraphDest
import core.ui.components.NetworkStatusBar

@Composable
fun MainNavigation(
    mainActivityViewModelFactory: MainActivityViewModelFactory,
    defaultNavigator: DefaultNavigator
) {
    val navController = rememberNavController()
    val mainActivityViewModel: MainActivityViewModel = viewModel(
        factory = mainActivityViewModelFactory
    )
    val isConnected by mainActivityViewModel.isConnected.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                navController = navController,
                startDestination = SubGraphDest.Expenses
            ) {
                defaultNavigator.featureExpenses.registerGraph(
                    navHostController = navController,
                    navGraphBuilder = this
                )
            }
        }
        NetworkStatusBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            isConnected = isConnected
        )
    }
}
