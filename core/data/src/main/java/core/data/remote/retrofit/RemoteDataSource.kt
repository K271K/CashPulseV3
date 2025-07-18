package core.data.remote.retrofit

import core.domain.model.transaction.TransactionDomainModel

interface RemoteDataSource {

    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?,
    ): List<TransactionDomainModel>

}