package feature.expenses.presentation.screens.expenses_today

import feature.expenses.presentation.model.ExpenseTodayUiModel

data class ExpensesTodayScreenState(
    val isLoading: Boolean,
    val error: String? = null,
    val expensesList: List<ExpenseTodayUiModel> = emptyList()
)