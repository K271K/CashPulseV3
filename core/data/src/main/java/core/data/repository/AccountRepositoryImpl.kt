package core.data.repository

import core.data.remote.retrofit.RemoteDataSource
import core.domain.model.account.AccountDomainModel
import core.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : AccountRepository {
    override suspend fun getAllAccounts(userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountById(accountId: Int): AccountDomainModel = withContext(
        Dispatchers.IO
    ) {
        return@withContext remoteDataSource.getAccountById(accountId = accountId)
    }

    override suspend fun updateAccount(accountId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(accountId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountHistory(accountId: Int) {
        TODO("Not yet implemented")
    }
}