package core.domain.model.transaction

data class CreateTransactionDomainModel(
    var accountId: Int,
    var categoryId: Int,
    var amount: String,
    var transactionDate: String,
    var comment: String
)

