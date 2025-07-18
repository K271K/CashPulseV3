package core.domain.usecase

import core.domain.model.transaction.TransactionDomainModel
import core.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<TransactionDomainModel> {
        return try {
            val transaction  = transactionRepository.getTransactionById(
                transactionId = transactionId
            )
            return Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}