package core.domain.usecase

import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        transactionId: Int,
        transaction: CreateTransactionDomainModel
    ) : Result<Unit> {
        return try {
            transactionRepository.updateTransaction(
                transactionId = transactionId,
                transaction = transaction
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}