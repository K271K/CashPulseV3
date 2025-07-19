package feature.incomes.presentation.screens.incomes_today

import feature.incomes.presentation.model.IncomeUiModel

data class IncomesTodayScreenState(
    val isLoading: Boolean,
    val error: String? = null,
    val expensesList: List<IncomeUiModel> = emptyList(),
    val totalAmount: String = "",
    val currency: String = ""
)