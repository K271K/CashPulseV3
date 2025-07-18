package core.navigation

import kotlinx.serialization.Serializable

/**
 * Здесь описаны верхнеуровневые маршруты. Одна фича - один маршрут верхнеуровневый для неё.
 */

sealed class SubGraphDest() {

    @Serializable
    data object Expenses: SubGraphDest()

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

}