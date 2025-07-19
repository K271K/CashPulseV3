package feature.account.domain.usecase

import core.domain.model.account.AccountDomainModel
import core.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountDataUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        newAccountData: AccountDomainModel
    ) : Result<AccountDomainModel> {
        try {
            val result = accountRepository.updateAccount(account = newAccountData)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}