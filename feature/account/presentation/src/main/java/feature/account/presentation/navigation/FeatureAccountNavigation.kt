package feature.account.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import feature.account.presentation.screens.account_main_screen.AccountScreen
import feature.account.presentation.screens.account_main_screen.AccountScreenViewModelFactory
import javax.inject.Inject

interface FeatureAccountNavigation : Feature

internal class FeatureAccountNavigationImpl @Inject constructor(
    private val accountScreenViewModelFactory: AccountScreenViewModelFactory
) : FeatureAccountNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Account>(startDestination = Dest.AccountMain) {
            composable<Dest.AccountMain> {
                AccountScreen(
                    accountScreenViewModelFactory = accountScreenViewModelFactory
                )
            }
        }
    }

}