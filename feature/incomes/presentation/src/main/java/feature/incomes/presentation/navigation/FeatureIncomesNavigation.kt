package feature.incomes.presentation.navigation

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

interface FeatureIncomesNavigation : Feature

internal class FeatureIncomesNavigationImpl @Inject constructor() : FeatureIncomesNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Incomes>(startDestination = Dest.IncomesToday) {
            composable<Dest.IncomesToday> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Incomes Main Screen")
                }
            }
        }
    }

}