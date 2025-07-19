package core.domain.usecase

import core.domain.model.account.AccountDomainModel
import core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke() : Result<List<AccountDomainModel>> {
        try {
            val accountsList = accountRepository.getAllAccounts()
            return Result.success(accountsList)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}