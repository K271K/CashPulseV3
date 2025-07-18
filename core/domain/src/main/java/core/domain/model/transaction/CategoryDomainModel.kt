package core.domain.model.transaction

data class CategoryDomainModel(
    val emoji: String,
    val id: Int,
    val isIncome: Boolean,
    val name: String
)
