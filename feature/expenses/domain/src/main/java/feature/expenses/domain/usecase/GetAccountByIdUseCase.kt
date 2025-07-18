package feature.expenses.domain.usecase

import core.domain.model.account.AccountDomainModel
import core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(accountId: Int) : Result<AccountDomainModel> {
        return try {
            val account = accountRepository.getAccountById(accountId = accountId)
            Result.success(account)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}