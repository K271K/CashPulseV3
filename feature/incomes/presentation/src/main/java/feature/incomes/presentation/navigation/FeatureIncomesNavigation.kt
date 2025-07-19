package feature.incomes.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import feature.incomes.presentation.screens.incomes_add.IncomesAddScreen
import feature.incomes.presentation.screens.incomes_add.IncomesAddScreenViewModelFactory
import feature.incomes.presentation.screens.incomes_edit.IncomesEditScreen
import feature.incomes.presentation.screens.incomes_edit.IncomesEditScreenViewModel
import feature.incomes.presentation.screens.incomes_edit.IncomesEditScreenViewModelFactory
import feature.incomes.presentation.screens.incomes_history.IncomesHistoryScreen
import feature.incomes.presentation.screens.incomes_history.IncomesHistoryScreenViewModelFactory
import feature.incomes.presentation.screens.incomes_today.IncomesTodayScreen
import feature.incomes.presentation.screens.incomes_today.IncomesTodayScreenViewModelFactory
import javax.inject.Inject

interface FeatureIncomesNavigation : Feature

internal class FeatureIncomesNavigationImpl @Inject constructor(
    private val incomesTodayScreenViewModelFactory: IncomesTodayScreenViewModelFactory,
    private val incomesHistoryScreenViewModelFactory: IncomesHistoryScreenViewModelFactory,
    private val incomesAddScreenViewModelFactory: IncomesAddScreenViewModelFactory,
    private val incomesEditScreenViewModelFactory: IncomesEditScreenViewModelFactory
) : FeatureIncomesNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Incomes>(startDestination = Dest.IncomesToday) {
            composable<Dest.IncomesToday> {
                IncomesTodayScreen(
                    incomesTodayScreenViewModelFactory = incomesTodayScreenViewModelFactory,
                    goToHistoryScreen = {
                        navHostController.navigate(Dest.IncomesHistory) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesToday) {
                                inclusive = true
                            }
                        }
                    },
                    goToAddIncomeScreen = {
                        navHostController.navigate(Dest.IncomesAdd) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesToday) {
                                inclusive = true
                            }
                        }
                    },
                    goToEditIncomeScreen = { incomeId ->
                        navHostController.navigate(Dest.IncomesEdit(incomeId = incomeId)) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesToday) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Dest.IncomesHistory> {
                IncomesHistoryScreen(
                    incomesHistoryScreenViewModelFactory = incomesHistoryScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.IncomesToday) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesHistory) {
                                inclusive = true
                            }
                        }
                    },
                    goToEditIncomeScreen = { incomeId ->
                        navHostController.navigate(Dest.IncomesEdit(incomeId = incomeId)) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesHistory) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Dest.IncomesAdd> {
                IncomesAddScreen(
                    incomesAddScreenViewModelFactory = incomesAddScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.IncomesToday) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesHistory) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
            composable<Dest.IncomesEdit> {
                val args = it.toRoute<Dest.IncomesEdit>()
                IncomesEditScreen (
                    expenseId = args.incomeId,
                    incomesEditScreenViewModelFactory = incomesEditScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.IncomesToday) {
                            launchSingleTop = true
                            popUpTo(Dest.IncomesEdit(args.incomeId)) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }

}