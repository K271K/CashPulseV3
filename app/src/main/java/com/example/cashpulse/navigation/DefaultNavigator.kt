package com.example.cashpulse.navigation

import feature.account.presentation.navigation.FeatureAccountNavigation
import feature.categories.presentation.navigation.FeatureCategoriesNavigation
import feature.expenses.presentation.navigation.FeatureExpensesNavigation
import feature.incomes.presentation.navigation.FeatureIncomesNavigation
import feature.settings.presentation.navigation.FeatureSettingsNavigation
import javax.inject.Inject

/**
 * Каждая переменная представлена интерфейсом, который наследуется от интерфейса Feature (:core:navigation модуль)
 * В каждом модуле есть реализация соответствующего интерфейса, в которой описывается граф навигации для каждой фичи
 * Грубо говоря переменные которые есть тут это 5 основных маршрутов навигации. Но каждый из них ещё содержит подграф
 */
data class DefaultNavigator @Inject constructor(
    val featureExpenses: FeatureExpensesNavigation,
    val featureIncomes: FeatureIncomesNavigation,
    val featureAccount: FeatureAccountNavigation,
    val featureCategories: FeatureCategoriesNavigation,
    val featureSettings: FeatureSettingsNavigation,
)
