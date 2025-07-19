package feature.account.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import core.navigation.Dest
import core.navigation.Feature
import core.navigation.SubGraphDest
import javax.inject.Inject

interface FeatureAccountNavigation : Feature

internal class FeatureAccountNavigationImpl @Inject constructor() : FeatureAccountNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Account>(startDestination = Dest.AccountMain) {
            composable<Dest.AccountMain> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Account Main Screen")
                }
            }
        }
    }

}