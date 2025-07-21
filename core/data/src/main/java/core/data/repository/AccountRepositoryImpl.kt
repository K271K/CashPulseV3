package core.data.repository

import android.util.Log
import core.data.local.dao.AccountDao
import core.data.local.entity.AccountEntity
import core.data.remote.connection.ConnectivityObserver
import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.account.AccountDomainModel
import core.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val connectivityObserver: ConnectivityObserver,
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun getAllAccounts(userId: Int): List<AccountDomainModel> =
        withContext(Dispatchers.IO) {

            // Если есть интернет - проверяем новые аккаунты с сервера  
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    val remoteAccounts = remoteDataSource.getAllAccounts(userId = userId)
                    Log.d(
                        "AccountRepositoryImpl",
                        "Аккаунты с сервера: ${remoteAccounts.map { it.id }}"
                    )

                    // Проверяем какие аккаунты новые
                    val existingAccountIds = accountDao.getAllAccounts().map { it.id }.toSet()
                    val newAccounts = remoteAccounts.filter { it.id !in existingAccountIds }

                    if (newAccounts.isNotEmpty()) {
                        Log.d(
                            "AccountRepositoryImpl",
                            "Найдено новых аккаунтов: ${newAccounts.size}"
                        )

                        val newAccountEntities = newAccounts.map { account ->
                            AccountEntity(
                                id = account.id,
                                name = account.name,
                                balance = account.balance,
                                currency = account.currency,
                                userId = account.userId,
                                createdAt = account.createdAt,
                                updatedAt = account.updatedAt
                            )
                        }
                        accountDao.insertAll(newAccountEntities)
                    } else {
                        Log.d("AccountRepositoryImpl", "Новых аккаунтов нет")
                    }

                } catch (e: Exception) {
                    Log.e("AccountRepositoryImpl", "Ошибка синхронизации: ${e.message}")
                }
            } else {
                Log.d("AccountRepositoryImpl", "Нет интернета, работаем с локальными данными")
            }

            // Всегда возвращаем данные из локальной БД
            return@withContext getAccountsFromLocalDb(userId)
    }

    override suspend fun getAccountById(accountId: Int): AccountDomainModel =
        withContext(Dispatchers.IO) {

            // Если есть интернет - пробуем обновить данные
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    val remoteAccount = remoteDataSource.getAccountById(accountId = accountId)
                    Log.d("AccountRepositoryImpl", "Аккаунт с сервера: ${remoteAccount.id}")

                    val accountEntity = AccountEntity(
                        id = remoteAccount.id,
                        name = remoteAccount.name,
                        balance = remoteAccount.balance,
                        currency = remoteAccount.currency,
                        userId = remoteAccount.userId,
                        createdAt = remoteAccount.createdAt,
                        updatedAt = remoteAccount.updatedAt
                    )
                    accountDao.insert(accountEntity)

                } catch (e: Exception) {
                    Log.e("AccountRepositoryImpl", "Ошибка получения аккаунта: ${e.message}")
                }
            }

            // Всегда возвращаем данные из локальной БД
            return@withContext getAccountFromLocalDb(accountId)
        }

    private suspend fun getAccountsFromLocalDb(userId: Int): List<AccountDomainModel> {
        val entities = accountDao.getAllAccounts()
        return entities.map { entity ->
            AccountDomainModel(
                id = entity.id,
                name = entity.name,
                balance = entity.balance,
                currency = entity.currency,
                userId = entity.userId,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }

    private suspend fun getAccountFromLocalDb(accountId: Int): AccountDomainModel {
        val entity = accountDao.getAccountById(accountId)
        return if (entity != null) {
            AccountDomainModel(
                id = entity.id,
                name = entity.name,
                balance = entity.balance,
                currency = entity.currency,
                userId = entity.userId,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        } else {
            // Fallback если аккаунта нет в БД
            AccountDomainModel(
                id = accountId,
                name = "Unknown Account",
                balance = "0",
                currency = "RUB",
                userId = null,
                createdAt = null,
                updatedAt = null
            )
        }
    }

    override suspend fun createAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun updateAccount(account: AccountDomainModel): AccountDomainModel =
        withContext(Dispatchers.IO) {
            if (connectivityObserver.isCurrentlyConnected()) {
                try {
                    // 1. Обновляем на сервере
                    val updatedAccount = remoteDataSource.updateAccount(account = account)
                    Log.d(
                        "AccountRepositoryImpl",
                        "Аккаунт обновлен на сервере: ${updatedAccount.id}"
                    )

                    // 2. Сразу обновляем в локальной БД
                    val accountEntity = AccountEntity(
                        id = updatedAccount.id,
                        name = updatedAccount.name,
                        balance = updatedAccount.balance,
                        currency = updatedAccount.currency,
                        userId = updatedAccount.userId,
                        createdAt = updatedAccount.createdAt,
                        updatedAt = updatedAccount.updatedAt
                    )
                    accountDao.insert(accountEntity)
                    Log.d("AccountRepositoryImpl", "Аккаунт обновлен в локальной БД")

                    return@withContext updatedAccount
                } catch (e: Exception) {
                    Log.e("AccountRepositoryImpl", "Ошибка обновления аккаунта: ${e.message}")
                    throw e
                }
            } else {
                throw Exception("Данная возможность недоступна без интернета :(")
            }

    }

    override suspend fun deleteAccount(accountId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountHistory(accountId: Int) {
        TODO("Not yet implemented")
    }

}