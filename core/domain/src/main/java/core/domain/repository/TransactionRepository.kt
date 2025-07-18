package core.domain.repository

import core.domain.model.transaction.TransactionDomainModel
import core.domain.utils.DomainConstants.ACCOUNT_ID

interface TransactionRepository {
    suspend fun getAccountTransactionsByPeriod(
        accountId: Int = ACCOUNT_ID,
        startDate: String,
        endDate: String
    ) : List<TransactionDomainModel>
}