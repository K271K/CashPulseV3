package feature.expenses.presentation.model

import core.domain.model.transaction.TransactionDomainModel

fun TransactionDomainModel.toExpenseTodayUiModel() = ExpenseTodayUiModel(
    id = this.id,
    categoryEmoji = this.category.emoji,
    categoryName = this.category.name,
    comment = this.comment,
    amount = this.amount,
    currency = currencyFromWordToSymbol(this.account.currency)
)

fun List<TransactionDomainModel>.toExpenseTodayUiModelList() = this.map { it.toExpenseTodayUiModel() }

fun currencyFromWordToSymbol(wordCurrency: String) =
    when (wordCurrency) {
        "USD" -> "$"
        "EUR" -> "€"
        "RUB" -> "£"
        else -> wordCurrency
    }