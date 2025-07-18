package core.data.repository

import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : TransactionRepository {
    override suspend fun getAccountTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDomainModel> = withContext(Dispatchers.IO) {
        val remoteData = remoteDataSource.getTransactionsByPeriod(
            accountId = accountId,
            startDate = startDate,
            endDate = endDate,
        )
        return@withContext remoteData
    }

    override suspend fun createTransaction(transaction: CreateTransactionDomainModel) {
        val result = remoteDataSource.createTransaction(
            transaction = transaction
        )
        println(result)
    }

    override suspend fun getTransactionById(transactionId: Int): TransactionDomainModel {
        return remoteDataSource.getTransactionById(transactionId)
    }

    override suspend fun deleteTransaction(transactionId: Int) {
        remoteDataSource.deleteTransaction(
            transactionId = transactionId
        )
    }

    override suspend fun updateTransaction(
        transaction: CreateTransactionDomainModel,
        transactionId: Int
    ) {
        remoteDataSource.updateTransaction(
            transaction = transaction,
            transactionId = transactionId
        )
    }
}