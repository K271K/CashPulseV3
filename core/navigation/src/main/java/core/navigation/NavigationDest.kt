package core.navigation

import kotlinx.serialization.Serializable

/**
 * Здесь описаны верхнеуровневые маршруты. Одна фича - один маршрут верхнеуровневый для неё.
 */

sealed class SubGraphDest() {

    @Serializable
    data object Expenses: SubGraphDest()

    @Serializable
    data object Incomes: SubGraphDest()

    @Serializable
    data object Account: SubGraphDest()

    @Serializable
    data object Category: SubGraphDest()

    @Serializable
    data object Settings: SubGraphDest()

}

/**
 * Здесь описаны вложенные графы навигации. У каждой фичи может быть своя навигация.
 */
sealed class Dest() {

    @Serializable
    data object ExpensesToday: Dest()

    @Serializable
    data object ExpensesHistory: Dest()

    @Serializable
    data class ExpensesEdit(val expenseId: Int): Dest()

    @Serializable
    data object ExpensesAdd: Dest()

    @Serializable
    data object IncomesToday: Dest()

    @Serializable
    data object IncomesHistory: Dest()

    @Serializable
    data class IncomesEdit(val incomeId: Int): Dest()

    @Serializable
    data object IncomesAdd: Dest()

    @Serializable
    data object AccountMain: Dest()

    @Serializable
    data class AccountEdit(val accountId: Int): Dest()

    @Serializable
    data object Category: Dest()

    @Serializable
    data object Settings: Dest()

}