package core.data.repository

import core.domain.model.transaction.AccountDomainModel
import core.domain.model.transaction.CategoryDomainModel
import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionsRepositoryImpl : TransactionRepository {
    override suspend fun getAccountTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionDomainModel> = withContext(Dispatchers.IO) {
        return@withContext listOf(
            TransactionDomainModel(
                account = AccountDomainModel(
                    balance = "1000",
                    currency = "RUB",
                    id = 211,
                    name = "Pay",
                    userId = 12,
                    createdAt = "",
                    updatedAt = ""
                ),
                amount = "100.00",
                category = CategoryDomainModel(
                    emoji = "🍔",
                    id = 8,
                    isIncome = false,
                    name = "Жратва"
                ),
                comment = "",
                createdAt = "",
                id = 1,
                transactionDate = "",
                updatedAt = ""
            ),
        )
    }
}