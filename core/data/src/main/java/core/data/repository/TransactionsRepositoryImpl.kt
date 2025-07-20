package core.data.repository

import android.util.Log
import core.data.local.dao.TransactionDao
import core.data.local.dao.AccountDao
import core.data.local.dao.CategoryDao
import core.data.local.entity.TransactionEntity
import core.data.local.entity.AccountEntity
import core.data.local.entity.CategoryEntity
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.account.AccountDomainModel
import core.domain.model.category.CategoryDomainModel
import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val connectivityObserver: ConnectivityObserver,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override suspend fun getAccountTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDomainModel> = withContext(Dispatchers.IO) {

        // Если есть интернет - проверяем новые транзакции с сервера
        if (connectivityObserver.isCurrentlyConnected()) {
            try {
                // 1. Запрос на сервер
                val remoteData = remoteDataSource.getTransactionsByPeriod(
                    accountId = accountId,
                    startDate = startDate,
                    endDate = endDate
                )
                Log.d(
                    "TransactionsRepositoryImpl",
                    "Транзакции с сервера: ${remoteData.map { it.id }}"
                )

                // 2. Проверяем какие транзакции новые
                val existingTransactionIds = transactionDao.getAllTransactionIds().toSet()
                val newTransactions = remoteData.filter { it.id !in existingTransactionIds }

                if (newTransactions.isNotEmpty()) {
                    Log.d(
                        "TransactionsRepositoryImpl",
                        "Найдено новых транзакций: ${newTransactions.size}"
                    )

                    // Сохраняем только новые транзакции
                    val newTransactionEntities = newTransactions.map { tx ->
                        TransactionEntity(
                            id = tx.id,
                            accountId = tx.account.id,
                            categoryId = tx.category.id,
                            amount = tx.amount,
                            comment = tx.comment,
                            transactionDate = tx.transactionDate,
                            createdAt = tx.createdAt,
                            updatedAt = tx.updatedAt
                        )
                    }
                    transactionDao.insertAll(newTransactionEntities)
                } else {
                    Log.d("TransactionsRepositoryImpl", "Новых транзакций нет")
                }

            } catch (e: Exception) {
                Log.e("TransactionsRepositoryImpl", "Ошибка синхронизации: ${e.message}")
            }
        } else {
            Log.d("TransactionsRepositoryImpl", "Нет интернета, работаем с локальными данными")
        }

        // 6. ВСЕГДА возвращаем данные из локальной БД
        return@withContext getFromLocalDb(accountId, startDate, endDate)
    }

    private suspend fun getFromLocalDb(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDomainModel> {
        // Преобразуем yyyy-mm-dd в ISO формат
        val startDateIso = startDate?.let { "${it}T00:00:00.000Z" } ?: "1900-01-01T00:00:00.000Z"
        val endDateIso = endDate?.let { "${it}T23:59:59.999Z" } ?: "2100-12-31T23:59:59.999Z"

        val entities = transactionDao.getByAccountAndPeriod(accountId, startDateIso, endDateIso)

        // TransactionEntity -> TransactionDomainModel с реальными данными
        return entities.map { entity ->
            // Получаем реальные данные аккаунта и категории из БД
            val accountEntity = accountDao.getAccountById(entity.accountId)
            val categoryEntity = categoryDao.getCategoryById(entity.categoryId)

            TransactionDomainModel(
                id = entity.id,
                amount = entity.amount,
                comment = entity.comment,
                transactionDate = entity.transactionDate,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                account = AccountDomainModel(
                    id = accountEntity?.id ?: entity.accountId,
                    name = accountEntity?.name ?: "Unknown",
                    balance = accountEntity?.balance ?: "0",
                    currency = accountEntity?.currency ?: "RUB",
                    userId = accountEntity?.userId,
                    createdAt = accountEntity?.createdAt,
                    updatedAt = accountEntity?.updatedAt
                ),
                category = CategoryDomainModel(
                    id = categoryEntity?.id ?: entity.categoryId,
                    name = categoryEntity?.name ?: "Unknown",
                    emoji = categoryEntity?.emoji ?: "❓",
                    isIncome = categoryEntity?.isIncome ?: false
                )
            )
        }
    }

    override suspend fun createTransaction(transaction: CreateTransactionDomainModel) {
        val result = remoteDataSource.createTransaction(
            transaction = transaction
        )
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