package feature.account.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import feature.account.presentation.screens.account_edit.AccountEditScreen
import feature.account.presentation.screens.account_edit.AccountEditScreenViewModelFactory
import feature.account.presentation.screens.account_main_screen.AccountScreen
import feature.account.presentation.screens.account_main_screen.AccountScreenViewModelFactory
import javax.inject.Inject

interface FeatureAccountNavigation : Feature

internal class FeatureAccountNavigationImpl @Inject constructor(
    private val accountScreenViewModelFactory: AccountScreenViewModelFactory,
    private val accountEditScreenViewModelFactory: AccountEditScreenViewModelFactory
) : FeatureAccountNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Account>(startDestination = Dest.AccountMain) {
            composable<Dest.AccountMain> {
                AccountScreen(
                    accountScreenViewModelFactory = accountScreenViewModelFactory,
                    goToEditAccount = { accountId->
                        navHostController.navigate(Dest.AccountEdit(accountId = accountId)) {
                            launchSingleTop = true
                            popUpTo(Dest.AccountMain) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Dest.AccountEdit> {
                val args = it.toRoute<Dest.AccountEdit>()
                AccountEditScreen(
                    accountId = args.accountId,
                    accountEditScreenViewModelFactory = accountEditScreenViewModelFactory,
                    goBack = {
                        navHostController.navigate(Dest.AccountMain) {
                            launchSingleTop = true
                            popUpTo(Dest.ExpensesEdit(args.accountId)) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }

}