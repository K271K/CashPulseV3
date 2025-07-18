package core.data.remote.retrofit

import core.domain.model.account.AccountDomainModel
import core.domain.model.category.CategoryDomainModel
import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.model.transaction.TransactionDomainModel

interface RemoteDataSource {

    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?,
    ): List<TransactionDomainModel>

    suspend fun getAllCategories(): List<CategoryDomainModel>

    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDomainModel>

    suspend fun getAccountById(accountId: Int): AccountDomainModel

    suspend fun createTransaction(transaction: CreateTransactionDomainModel)

    suspend fun getTransactionById(transactionId: Int): TransactionDomainModel

    suspend fun updateTransaction(
        transaction: CreateTransactionDomainModel,
        transactionId: Int
    )

    suspend fun deleteTransaction(
        transactionId: Int
    )

}