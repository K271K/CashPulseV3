package core.domain.model.transaction

import core.domain.model.account.AccountDomainModel
import core.domain.model.category.CategoryDomainModel

data class TransactionDomainModel(
    val account: AccountDomainModel,
    val amount: String,
    val category: CategoryDomainModel,
    val comment: String,
    val createdAt: String,
    val id: Int,
    val transactionDate: String,
    val updatedAt: String
)
