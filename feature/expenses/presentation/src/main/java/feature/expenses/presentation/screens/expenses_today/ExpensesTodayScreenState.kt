package feature.expenses.presentation.screens.expenses_today

import feature.expenses.presentation.model.ExpenseUiModel

data class ExpensesTodayScreenState(
    val isLoading: Boolean,
    val error: String? = null,
    val expensesList: List<ExpenseUiModel> = emptyList(),
    val totalAmount: String = "",
    val currency: String = ""
)