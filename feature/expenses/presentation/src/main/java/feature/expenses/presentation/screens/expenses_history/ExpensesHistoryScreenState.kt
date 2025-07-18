package feature.expenses.presentation.screens.expenses_history

import feature.expenses.presentation.model.ExpenseUiModel

data class ExpensesHistoryScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val expensesList: List<ExpenseUiModel> = emptyList(),
    val totalAmount: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val currency: String = ""
)
