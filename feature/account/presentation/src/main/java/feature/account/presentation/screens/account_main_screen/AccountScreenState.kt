package feature.account.presentation.screens.account_main_screen

import androidx.compose.runtime.Immutable
import feature.account.presentation.model.AccountScreenItem

data class AccountScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val accountsList: List<AccountScreenItem> = emptyList(),
)

