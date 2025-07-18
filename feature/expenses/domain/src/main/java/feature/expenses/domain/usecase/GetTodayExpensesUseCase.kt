package feature.expenses.domain.usecase

import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTodayExpensesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke() : Result<List<TransactionDomainModel>> {
        return try {
            val domainTransactionList = transactionRepository.getAccountTransactionsByPeriod(
                startDate = "2025-07-18",
                endDate = "2025-07-18"
            )
            val filteredList = domainTransactionList.filter { !it.category.isIncome }
            Result.success(filteredList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}