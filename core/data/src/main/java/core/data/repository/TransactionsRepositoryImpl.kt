package core.data.repository

import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.transaction.AccountDomainModel
import core.domain.model.transaction.CategoryDomainModel
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
}