package feature.expenses.domain.usecase

import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import javax.inject.Inject

//TODO AccountId
/** Где-то тут должна быть логика подтягивания ID счёта, который выбран в данный момент.
* Но пока что можно пользоваться одним счётом, поэтому ID счёта просто хардкодится
* в константах и используется в репозитории
*/
class GetPeriodExpensesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        startDate: String?,
        endDate: String?
    ) : Result<List<TransactionDomainModel>> {
        return try {
            val domainTransactionList = transactionRepository.getAccountTransactionsByPeriod(
                startDate = startDate,
                endDate = endDate
            )
            val filteredList = domainTransactionList.filter { !it.category.isIncome }
            Result.success(filteredList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}