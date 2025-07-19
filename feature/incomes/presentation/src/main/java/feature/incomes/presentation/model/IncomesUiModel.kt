package feature.incomes.presentation.model

/**
 * UI модель которая используется на экране
 * Расходы сегодня
 * История расходов
 */
data class IncomeUiModel(
    val id: Int,
    val categoryEmoji: String? = null,
    val categoryName: String,
    val comment: String,
    val amount: String,
    val currency: String,
    val time: String? = null,
    val date: String? = null,
)


