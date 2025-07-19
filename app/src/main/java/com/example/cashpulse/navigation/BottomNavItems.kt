package com.example.cashpulse.navigation

import core.navigation.SubGraphDest
import core.ui.R

data class BottomNavItem(
    val route: SubGraphDest,
    val icon: Int,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = SubGraphDest.Expenses,
        icon = R.drawable.downtrend,
        label = "Расходы"
    ),
    BottomNavItem(
        route = SubGraphDest.Incomes,
        icon = R.drawable.uptrend,
        label = "Доходы"
    ),
    BottomNavItem(
        route = SubGraphDest.Account,
        icon = R.drawable.calculator,
        label = "Счёт"
    ),
    BottomNavItem(
        route = SubGraphDest.Category,
        icon = (R.drawable.barchartside),
        label = "Статьи"
    ),
    BottomNavItem(
        route = SubGraphDest.Settings,
        icon = (R.drawable.settings),
        label = "Настройки"
    )
)