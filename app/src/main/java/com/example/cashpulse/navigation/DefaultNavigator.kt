package com.example.cashpulse.navigation

import feature.expenses.presentation.navigation.FeatureExpensesNavigation

/**
 * Каждая переменная представлена интерфейсом, который наследуется от интерфейса Feature (:core:navigation модуль)
 * В каждом модуле есть реализация соответствующего интерфейса, в которой описывается граф навигации для каждой фичи
 * Грубо говоря переменные которые есть тут это 5 основных маршрутов навигации. Но каждый из них ещё содержит подграф
 */
data class DefaultNavigator(
    val featureExpenses: FeatureExpensesNavigation
)
