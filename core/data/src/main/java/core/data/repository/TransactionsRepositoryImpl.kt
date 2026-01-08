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
                            accountId = tx.account?.id ?: accountId, // fallback на параметр
                            categoryId = tx.category?.id ?: 1, // fallback
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

    override suspend fun createTransaction(transaction: CreateTransactionDomainModel): TransactionDomainModel =
        withContext(Dispatchers.IO) {
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    // 1. Создаем транзакцию на сервере и получаем её
                    val createdTransaction =
                        remoteDataSource.createTransaction(transaction = transaction)
                    Log.d(
                        "TransactionsRepositoryImpl",
                        "Транзакция создана на сервере: ${createdTransaction.id}"
                    )

                    // 2. Сразу сохраняем созданную транзакцию в локальную БД
                    val transactionEntity = TransactionEntity(
                        id = createdTransaction.id,
                        accountId = createdTransaction.account?.id ?: transaction.accountId,
                        categoryId = createdTransaction.category?.id ?: transaction.categoryId,
                        amount = createdTransaction.amount,
                        comment = createdTransaction.comment,
                        transactionDate = createdTransaction.transactionDate,
                        createdAt = createdTransaction.createdAt,
                        updatedAt = createdTransaction.updatedAt
                    )
                    transactionDao.insert(transactionEntity)
                    Log.d("TransactionsRepositoryImpl", "Транзакция сохранена в локальную БД")

                    return@withContext createdTransaction

                } catch (e: Exception) {
                    Log.e("TransactionsRepositoryImpl", "Ошибка создания транзакции: ${e.message}")
                    throw e
                }
            } else {
                throw Exception("Создание транзакций недоступно без интернета :(")
            }
        }

    override suspend fun getTransactionById(transactionId: Int): TransactionDomainModel =
        withContext(Dispatchers.IO) {
            // При наличии интернета - обновляем данные
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    val remoteTransaction = remoteDataSource.getTransactionById(transactionId)
                    Log.d(
                        "TransactionsRepositoryImpl",
                        "Транзакция получена с сервера: ${remoteTransaction.id}"
                    )

                    // Сохраняем обновленную транзакцию в БД
                    val transactionEntity = TransactionEntity(
                        id = remoteTransaction.id,
                        accountId = remoteTransaction.account?.id
                            ?: remoteTransaction.id, // fallback
                        categoryId = remoteTransaction.category?.id ?: 1, // fallback
                        amount = remoteTransaction.amount,
                        comment = remoteTransaction.comment,
                        transactionDate = remoteTransaction.transactionDate,
                        createdAt = remoteTransaction.createdAt,
                        updatedAt = remoteTransaction.updatedAt
                    )
                    transactionDao.insert(transactionEntity)

                    return@withContext remoteTransaction
                } catch (e: Exception) {
                    Log.e("TransactionsRepositoryImpl", "Ошибка получения транзакции: ${e.message}")
                }
            }

            // Всегда пытаемся получить из локальной БД
            val localTransaction = getTransactionFromLocalDb(transactionId)
            return@withContext localTransaction ?: throw Exception("Транзакция не найдена")
        }

    override suspend fun deleteTransaction(transactionId: Int): Unit = withContext(Dispatchers.IO) {
        if (connectivityObserver.isCurrentlyConnected()) {
            try {
                // 1. Удаляем на сервере
                remoteDataSource.deleteTransaction(transactionId = transactionId)
                Log.d("TransactionsRepositoryImpl", "Транзакция удалена на сервере: $transactionId")

                // 2. Сразу помечаем как удаленную в локальной БД (soft delete)
                transactionDao.softDeleteById(transactionId)
                Log.d("TransactionsRepositoryImpl", "Транзакция помечена как удаленная в БД")

            } catch (e: Exception) {
                Log.e("TransactionsRepositoryImpl", "Ошибка удаления транзакции: ${e.message}")
                throw e
            }
        } else {
            throw Exception("Удаление транзакций недоступно без интернета :(")
        }
    }

    override suspend fun updateTransaction(
        transaction: CreateTransactionDomainModel,
        transactionId: Int
    ): Unit = withContext(Dispatchers.IO) {
        if (connectivityObserver.isCurrentlyConnected()) {
            try {
                // 1. Обновляем на сервере
                remoteDataSource.updateTransaction(
                    transaction = transaction,
                    transactionId = transactionId
                )
                Log.d(
                    "TransactionsRepositoryImpl",
                    "Транзакция обновлена на сервере: $transactionId"
                )

                // 2. Получаем обновленную транзакцию и сохраняем в БД
                val updatedTransaction = remoteDataSource.getTransactionById(transactionId)
                val transactionEntity = TransactionEntity(
                    id = updatedTransaction.id,
                    accountId = updatedTransaction.account?.id ?: transaction.accountId,
                    categoryId = updatedTransaction.category?.id ?: transaction.categoryId,
                    amount = updatedTransaction.amount,
                    comment = updatedTransaction.comment,
                    transactionDate = updatedTransaction.transactionDate,
                    createdAt = updatedTransaction.createdAt,
                    updatedAt = updatedTransaction.updatedAt
                )
                transactionDao.insert(transactionEntity)
                Log.d("TransactionsRepositoryImpl", "Транзакция обновлена в локальной БД")

            } catch (e: Exception) {
                Log.e("TransactionsRepositoryImpl", "Ошибка обновления транзакции: ${e.message}")
                throw e
            }
        } else {
            throw Exception("Редактирование транзакций недоступно без интернета :(")
        }
    }

    private suspend fun getTransactionFromLocalDb(transactionId: Int): TransactionDomainModel? {
        val entity = transactionDao.getById(transactionId) ?: return null

        val accountEntity = accountDao.getAccountById(entity.accountId)
        val categoryEntity = categoryDao.getCategoryById(entity.categoryId)

        return TransactionDomainModel(
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