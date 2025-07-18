package core.domain.model.account

import core.domain.model.category.CategoryStatsModel

data class AccountDomainModel(
    val balance: String,
    val currency: String,
    val id: Int,
    val name: String,
    val userId: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val incomeStats: List<CategoryStatsModel>? = null,
    val expenseStats: List<CategoryStatsModel>? = null,
)