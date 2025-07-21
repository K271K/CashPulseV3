package feature.expenses.domain.usecase

import android.util.Log
import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import core.domain.utils.formatDateFromLongToHuman
import javax.inject.Inject

class GetTodayExpensesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke() : Result<List<TransactionDomainModel>> {
        return try {
            val todayDate = formatDateFromLongToHuman(System.currentTimeMillis())
            val domainTransactionList = transactionRepository.getAccountTransactionsByPeriod(
                startDate = todayDate,
                endDate = todayDate
            )
            val filteredList = domainTransactionList.filter { !it.category.isIncome }
            Log.d("getAccountTransactionsByPeriod", filteredList.toString())
            Result.success(filteredList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}