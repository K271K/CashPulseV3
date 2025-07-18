package core.domain.model.category

data class CategoryStatsModel(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: Boolean,
)
