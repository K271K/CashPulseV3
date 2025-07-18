package feature.expenses.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import core.ui.screens.transaction_add.TransactionAddScreen
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreen
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreen
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModelFactory
import javax.inject.Inject

interface FeatureExpensesNavigation : Feature

internal class FeatureExpensesNavigationImpl @Inject constructor(
    private val expensesTodayScreenViewModelFactory: ExpensesTodayScreenViewModelFactory
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
                            popUpTo(Dest.ExpensesHistory) {
                                inclusive = true
                            }
                        }
                    },
                    goToAddTransactionScreen = {
                        navHostController.navigate(Dest.TransactionAdd) {
                            launchSingleTop = true
                            popUpTo(Dest.TransactionAdd(isIncome = false)) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Dest.ExpensesHistory> {
                ExpensesHistoryScreen()
            }
        }
    }
}