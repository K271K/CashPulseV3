package core.domain.usecase

import core.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<Boolean> {
        try {
            transactionRepository.deleteTransaction(
                transactionId = transactionId
            )
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}