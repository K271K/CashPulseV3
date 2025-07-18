package core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Интерфейс от которого будет наследоваться каждая фича и определять граф навигации для себя
 */
interface Feature {

    fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    )

}