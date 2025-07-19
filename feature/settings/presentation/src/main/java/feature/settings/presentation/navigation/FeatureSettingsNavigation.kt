package feature.settings.presentation.navigation

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

interface FeatureSettingsNavigation : Feature

internal class FeatureSettingsNavigationImpl @Inject constructor() : FeatureSettingsNavigation {
    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SubGraphDest.Settings>(startDestination = Dest.Settings) {
            composable<Dest.Settings> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Settings Main Screen")
                }
            }
        }
    }

}