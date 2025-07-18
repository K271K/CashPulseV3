package feature.expenses.presentation.model

import core.domain.model.transaction.TransactionDomainModel
import core.domain.utils.formatCurrencyFromTextToSymbol
import core.domain.utils.formatISO8601ToDate
import core.domain.utils.formatISO8601ToTime

fun TransactionDomainModel.toExpenseUiModel() = ExpenseUiModel(
    id = this.id,
    categoryEmoji = this.category.emoji,
    categoryName = this.category.name,
    comment = this.comment,
    amount = this.amount,
    currency = formatCurrencyFromTextToSymbol(this.account.currency),
    time = formatISO8601ToTime(this.transactionDate),
    date = formatISO8601ToDate(this.transactionDate)
)

fun List<TransactionDomainModel>.toExpenseUiModelList() = this.map { it.toExpenseUiModel() }