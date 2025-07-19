package feature.incomes.presentation.screens.incomes_history

import feature.incomes.presentation.model.IncomeUiModel

data class IncomesHistoryScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val expensesList: List<IncomeUiModel> = emptyList(),
    val totalAmount: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val currency: String = ""
)
