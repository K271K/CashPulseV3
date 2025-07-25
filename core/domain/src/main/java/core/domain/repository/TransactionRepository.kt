package core.domain.repository

import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.model.transaction.TransactionDomainModel
import core.domain.utils.DomainConstants.ACCOUNT_ID

interface TransactionRepository {
    suspend fun getAccountTransactionsByPeriod(
        accountId: Int = ACCOUNT_ID,
        startDate: String?,
        endDate: String?
    ) : List<TransactionDomainModel>

    suspend fun createTransaction(transaction: CreateTransactionDomainModel)

    suspend fun getTransactionById(
        transactionId: Int,
    ): TransactionDomainModel

    suspend fun deleteTransaction(transactionId: Int)

    suspend fun updateTransaction(transaction: CreateTransactionDomainModel, transactionId: Int)

}