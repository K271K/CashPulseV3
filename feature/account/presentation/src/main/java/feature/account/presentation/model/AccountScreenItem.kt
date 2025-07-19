package feature.account.presentation.model

import androidx.compose.runtime.Immutable
import core.domain.model.account.AccountDomainModel


/**
 * Модель которая используется на экране отображения счетов
 */
@Immutable
data class AccountScreenItem(
    val id: Int,
    val name: String,
    val currency: String,
    val balance: String,
    val isSelected: Boolean
)

fun AccountDomainModel.toAccountScreenItem(accountId: Int) =
    AccountScreenItem(
        id = this.id,
        name = this.name,
        currency = this.currency,
        balance = this.balance,
        isSelected = this.id == accountId
    )

fun List<AccountDomainModel>.toAccountScreenItemList(accountId: Int) = this.map {
    it.toAccountScreenItem(accountId = accountId)
}
