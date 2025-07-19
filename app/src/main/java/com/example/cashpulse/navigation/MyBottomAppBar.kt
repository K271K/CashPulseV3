package com.example.cashpulse.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyBottomAppBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BottomAppBar {
        bottomNavItems.forEach { item->
            NavigationBarItem(
                selected = navBackStackEntry?.destination?.hierarchy?.any{
                    it.route == item.route::class.qualifiedName
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(0) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                label = { Text(text = item.label)},
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = null
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}