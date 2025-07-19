package feature.incomes.presentation.screens.incomes_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.usecase.CreateTransactionUseCase
import core.domain.usecase.GetAccountByIdUseCase
import core.domain.utils.DomainConstants.ACCOUNT_ID
import core.domain.utils.formatCurrencyFromTextToSymbol
import core.domain.utils.formatDateFromLongToHuman
import core.domain.utils.formatDateToISO8061
import core.ui.model.CategoryPickerUiModel
import core.ui.model.toCategoryPickerUiModel
import feature.incomes.domain.usecase.GetIncomesCategoriesUseCase
import feature.incomes.presentation.screens.EditIncomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

/**
 * ViewModel и фабрика для неё находятся в одном файле.
 * Мне удобно так ориентироваться по коду.
 * Не вижу смысла разделять их по разным файлам.
 */
class IncomesAddScreenViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getIncomesCategoriesUseCase: GetIncomesCategoriesUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditIncomeScreenState(isLoading = true))
    val uiState: StateFlow<EditIncomeScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getIncomesCategoriesUseCase()
                .onSuccess { categories ->
                    _uiState.update {
                        it.copy(categories = categories.toCategoryPickerUiModel())
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(error = e.message, isLoading = false)
                        return@launch
                    }
                }

            getAccountByIdUseCase(accountId = ACCOUNT_ID)
                .onSuccess { account ->
                    _uiState.update {
                        it.copy(
                            accountName = account.name,
                            currency = formatCurrencyFromTextToSymbol(account.currency),
                            isLoading = false
                        )
                    }
                }
                .onFailure { error->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                        return@launch
                    }
                }
        }
    }

    fun validateAndCreateTransaction(
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
            val dateISOFormatted = formatDateToISO8061(
                date = _uiState.value.expenseDate,
                time = _uiState.value.expenseTime
            )
            val createTransactionDomainModel = CreateTransactionDomainModel(
                accountId = ACCOUNT_ID,
                categoryId = _uiState.value.selectedCategory!!.id,
                amount = _uiState.value.amount,
                transactionDate = dateISOFormatted,
                comment = _uiState.value.comment
            )
            createTransactionUseCase(
                transaction = createTransactionDomainModel
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(success = true, isLoading = false, error = null)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(error = e.message, isLoading = false)
                    }
                }
        }

    }

    fun updateAmount(amount: String) {
        _uiState.update {
            it.copy(amount = amount)
        }
    }

    fun updateDate(dateInMillis: Long) {
        val formattedDate = formatDateFromLongToHuman(date = dateInMillis)
        _uiState.update {
            it.copy(expenseDate = formattedDate)
        }
    }

    fun updateCategory(category: CategoryPickerUiModel) {
        _uiState.update {
            it.copy(selectedCategory = category, categoryName = category.name)
        }
    }

    fun updateTime(hour: Int, minute: Int) {
        val formattedTime = String.format("%02d:%02d", hour, minute)
        _uiState.update {
            it.copy(expenseTime = formattedTime)
        }
    }

    fun updateComment(comment: String) {
        _uiState.update {
            it.copy(comment = comment)
        }
    }

}

class IncomesAddScreenViewModelFactory @Inject constructor(
    private val incomesAddScreenViewModelProvider: Provider<IncomesAddScreenViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return incomesAddScreenViewModelProvider.get() as T
    }
}