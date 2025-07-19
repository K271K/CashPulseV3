package feature.expenses.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import feature.expenses.presentation.screens.expenses_add.ExpensesAddScreen
import feature.expenses.presentation.screens.expenses_add.ExpensesAddScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_edit.ExpensesEditScreen
import feature.expenses.presentation.screens.expenses_edit.ExpensesEditScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreen
import feature.expenses.presentation.screens.expenses_history.ExpensesHistoryScreenViewModelFactory
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreen
import feature.expenses.presentation.screens.expenses_today.ExpensesTodayScreenViewModelFactory
import javax.inject.Inject

interface FeatureExpensesNavigation : Feature

internal class FeatureExpensesNavigationImpl @Inject constructor(
    private val expensesTodayScreenViewModelFactory: ExpensesTodayScreenViewModelFactory,
    private val expensesHistoryScreenViewModelFactory: ExpensesHistoryScreenViewModelFactory,
    private val expensesAddScreenViewModelFactory: ExpensesAddScreenViewModelFactory,
    private val expensesEditScreenViewModelFactory: ExpensesEditScreenViewModelFactory
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
                        }
                    },
                    goToAddExpenseScreen = {
                        navHostController.navigate(Dest.ExpensesAdd) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesToday) {
                                inclusive = true
                            }
                        }
                    },
                    goToEditExpenseScreen = { expenseId ->
                        navHostController.navigate(Dest.ExpensesEdit(expenseId = expenseId)) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesToday) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Dest.ExpensesHistory> {
                ExpensesHistoryScreen(
                    expensesHistoryScreenViewModelFactory = expensesHistoryScreenViewModelFactory,
                    goBack = {
                        navHostController.popBackStack()
                    },
                    goToEditExpenseScreen = { expenseId ->
                        navHostController.navigate(Dest.ExpensesEdit(expenseId = expenseId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<Dest.ExpensesAdd> {
                ExpensesAddScreen(
                    expensesAddScreenViewModelFactory = expensesAddScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.ExpensesToday) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesAdd) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Dest.ExpensesEdit> {
                val args = it.toRoute<Dest.ExpensesEdit>()
                ExpensesEditScreen(
                    expenseId = args.expenseId,
                    expensesEditScreenViewModelFactory = expensesEditScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.ExpensesToday) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesEdit(args.expenseId)) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}