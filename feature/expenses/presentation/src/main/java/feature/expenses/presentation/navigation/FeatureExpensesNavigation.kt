package feature.expenses.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
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
                    expensesTodayScreenViewModelFactory = expensesTodayScreenViewModelFactory
                )
            }
        }
    }
}