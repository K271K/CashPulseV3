package feature.account.presentation.screens.account_main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.usecase.GetAccountsUseCase
import core.domain.utils.DomainConstants.ACCOUNT_ID
import feature.account.presentation.model.toAccountScreenItemList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AccountScreenViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<AccountScreenState>(AccountScreenState(isLoading = true))
    val uiState: StateFlow<AccountScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAccountsUseCase()
                .onSuccess { listOfAccounts ->
                    val accountsList = listOfAccounts.toAccountScreenItemList(accountId = ACCOUNT_ID)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            accountsList = accountsList
                        )
                    }
                }
                .onFailure { e->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message,
                        )
                    }
                }
        }
    }
}

class AccountScreenViewModelFactory @Inject constructor(
    private val accountScreenViewModel: Provider<AccountScreenViewModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return accountScreenViewModel.get() as T
    }
}