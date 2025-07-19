package feature.account.presentation.screens.account_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.model.account.AccountDomainModel
import core.domain.usecase.GetAccountByIdUseCase
import feature.account.domain.usecase.UpdateAccountDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AccountEditScreenViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountEditScreenState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun initWithAccountId(accountId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )
            }
            getAccountByIdUseCase(accountId = accountId)
                .onSuccess { loadedAccount ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            id = loadedAccount.id,
                            name = loadedAccount.name,
                            balance = loadedAccount.balance,
                            currency = loadedAccount.currency,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message,
                            success = false
                        )
                    }
                }
        }
    }

    fun validateAndUpdateAccount(
        onValidationError: (String) -> Unit
    ) {
        viewModelScope.launch {

            val validationErrors = _uiState.value.getValidationErrors()
            if (validationErrors.isNotEmpty()) {
                val errorMessage = validationErrors.values.first()
                onValidationError(errorMessage)
                return@launch
            }

            _uiState.update {
                it.copy(isLoading = true)
            }

            val currentUiState = _uiState.value
            val account = AccountDomainModel(
                id = currentUiState.id,
                name = currentUiState.name,
                balance = currentUiState.balance,
                currency = currentUiState.currency,
                userId = null,
                createdAt = null,
                updatedAt = null
            )
            updateAccountUseCase(
                newAccountData = account
            )
                .onSuccess { updatedAccount ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            success = true,
                            id = updatedAccount.id,
                            name = updatedAccount.name,
                            balance = updatedAccount.balance,
                            currency = updatedAccount.currency
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message,
                            success = false
                        )
                    }
                }
        }
    }

    fun updateAccountName(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun updateAccountCurrency(newCurrency: String) {
        _uiState.update { it.copy(currency = newCurrency) }
    }

    fun updateAccountBalance(newBalance: String) {
        _uiState.update { it.copy(balance = newBalance) }
    }
}

class AccountEditScreenViewModelFactory @Inject constructor(
    private val accountEditScreenViewModelProvider: Provider<AccountEditScreenViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return accountEditScreenViewModelProvider.get() as T
    }
}