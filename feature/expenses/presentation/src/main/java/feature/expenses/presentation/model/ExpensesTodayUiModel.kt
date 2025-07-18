package feature.expenses.presentation.model

data class ExpenseTodayUiModel(
    val id: Int,
    val categoryEmoji: String,
    val categoryName: String,
    val comment: String,
    val amount: String,
    val currency: String
)


