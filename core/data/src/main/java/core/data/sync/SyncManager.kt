package core.data.sync

import android.util.Log
import core.data.local.dao.AccountDao
import core.data.local.dao.CategoryDao
import core.data.local.dao.TransactionDao
import core.data.local.entity.AccountEntity
import core.data.local.entity.CategoryEntity
import core.data.local.entity.TransactionEntity
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.domain.utils.DomainConstants.ACCOUNT_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val connectivityObserver: ConnectivityObserver,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) {

    suspend fun performInitialSync(): Result<Unit> = withContext(Dispatchers.IO) {
        if (!connectivityObserver.isCurrentlyConnected()) {
            return@withContext Result.failure(Exception("No internet connection"))
        }

        return@withContext try {
            Log.d("SyncManager","Starting initial sync...")

            // 1. Синхронизируем категории
            syncCategories()

            // 2. Синхронизируем аккаунты
            syncAccounts()

            // 3. Синхронизируем все транзакции
            syncAllTransactions()

            Log.d("SyncManager","Initial sync completed successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("SyncManager","Initial sync failed: ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun syncCategories() {
        val remoteCategories = remoteDataSource.getAllCategories()
        val categoryEntities = remoteCategories.map { category->
            CategoryEntity(
                id = category.id,
                name = category.name,
                emoji = category.emoji,
                isIncome = category.isIncome
                // поля sync можно не заполнять, подставится дефолт
            )
        }
        categoryDao.deleteAll() // Полная очистка, если структура категорий стабильно не меняется с бэка
        categoryDao.insertAll(categoryEntities)
        Log.d("SyncManager","Categories synced: ${categoryEntities.size}")
    }

    private suspend fun syncAccounts() {
        val remoteAccounts = remoteDataSource.getAllAccounts()
        val accountEntities = remoteAccounts.map { acc ->
            AccountEntity(
                id = acc.id,
                name = acc.name,
                balance = acc.balance,
                currency = acc.currency,
                userId = acc.userId,
                createdAt = acc.createdAt,
                updatedAt = acc.updatedAt
                // sync-поля не трогаем, дефолт
            )
        }
        accountDao.deleteAll() // Очищаем старые локальные аккаунты
        accountDao.insertAll(accountEntities)
        Log.d("SyncManager","Accounts synced: ${accountEntities.size}")
    }

    private suspend fun syncAllTransactions() {
        // Синхронизируем ВСЕ транзакции за большой диапазон дат
        val remoteTransactions = remoteDataSource.getTransactionsByPeriod(
            accountId = ACCOUNT_ID, // если 0 — значит все счета, либо убери если нужно по каждому отдельно
            startDate = "2020-01-01",
            endDate = "2030-12-31"
        )
        val transactionEntities = remoteTransactions.map { tx ->
            TransactionEntity(
                id = tx.id,
                accountId = tx.account.id,
                categoryId = tx.category.id,
                amount = tx.amount,
                comment = tx.comment,
                transactionDate = tx.transactionDate,
                createdAt = tx.createdAt,
                updatedAt = tx.updatedAt
                // остальные sync-поля не трогаем, дефолтные
            )
        }
        transactionDao.deleteAll()
        transactionDao.insertAll(transactionEntities)
        Log.d("SyncManager","Transactions synced: ${transactionEntities.size}")
    }

}
