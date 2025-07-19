package core.domain.repository

import core.domain.model.account.AccountDomainModel

interface AccountRepository {

    suspend fun getAllAccounts(userId: Int = 12) : List<AccountDomainModel>

    suspend fun createAccount()

    suspend fun getAccountById(accountId: Int) : AccountDomainModel

    suspend fun updateAccount(account: AccountDomainModel): AccountDomainModel

    suspend fun deleteAccount(accountId: Int)

    suspend fun getAccountHistory(accountId: Int)

}