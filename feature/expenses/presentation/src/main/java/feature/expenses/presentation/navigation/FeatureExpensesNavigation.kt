package feature.expenses.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreen
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreen
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModelFactory
import javax.inject.Inject

interface FeatureExpensesNavigation : Feature

internal class FeatureExpensesNavigationImpl @Inject constructor(
    private val expensesTodayScreenViewModelFactory: ExpensesTodayScreenViewModelFactory,
    private val expensesHistoryScreenViewModelFactory: ExpensesHistoryScreenViewModelFactory,
) : FeatureExpensesNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Expenses>(startDestination = Dest.ExpensesToday) {
            composable<Dest.ExpensesToday> {
                ExpensesTodayScreen(
                    expensesTodayScreenViewModelFactory = expensesTodayScreenViewModelFactory,
                    goToHistoryScreen = {
                        navHostController.navigate(Dest.ExpensesHistory) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesToday) {
                                inclusive = true
                            }
                        }
                    },
                    goToAddTransactionScreen = {

                    }
                )
            }
            composable<Dest.ExpensesHistory> {
                ExpensesHistoryScreen(
                    expensesHistoryScreenViewModelFactory = expensesHistoryScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.ExpensesToday) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesHistory) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}