package core.domain.usecase

import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor (
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(transaction: CreateTransactionDomainModel) : Result<Boolean> {
        println(transaction.transactionDate)
        try {
            transactionRepository.createTransaction(
                transaction = transaction
            )
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}