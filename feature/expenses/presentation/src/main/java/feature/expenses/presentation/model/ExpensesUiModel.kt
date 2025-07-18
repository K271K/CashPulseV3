package feature.expenses.presentation.model

/**
 * UI модель которая используется на экране
 * Расходы сегодня
 * История расходов
 */
data class ExpenseUiModel(
    val id: Int,
    val categoryEmoji: String,
    val categoryName: String,
    val comment: String,
    val amount: String,
    val currency: String,
    val time: String? = null,
    val date: String? = null,
)


