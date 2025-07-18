package feature.expenses.presentation.screens.expenses_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.model.transaction.CreateTransactionDomainModel
import core.domain.usecase.DeleteTransactionUseCase
import core.domain.usecase.GetTransactionByIdUseCase
import core.domain.usecase.UpdateTransactionUseCase
import core.domain.utils.DomainConstants.ACCOUNT_ID
import core.domain.utils.formatDateFromLongToHuman
import core.domain.utils.formatDateToISO8061
import core.domain.utils.formatISO8601ToDate
import core.domain.utils.formatISO8601ToTime
import core.ui.model.CategoryPickerUiModel
import core.ui.model.toCategoryPickerUiModel
import feature.expenses.domain.usecase.GetExpenseCategoriesUseCase
import feature.expenses.presentation.screens.EditExpenseScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ExpensesEditScreenViewModel @Inject constructor(
    private val getExpenseCategoriesUseCase: GetExpenseCategoriesUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditExpenseScreenState(isLoading = true))
    val uiState: StateFlow<EditExpenseScreenState> = _uiState.asStateFlow()

    fun initWithId(expenseId: Int) {
        viewModelScope.launch {
            getExpenseCategoriesUseCase()
                .onSuccess { categories ->
                    val mappedCategories = categories.toCategoryPickerUiModel()
                    _uiState.update {
                        it.copy(categories = mappedCategories)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                        return@launch
                    }
                }

            getTransactionByIdUseCase(transactionId = expenseId)
                .onSuccess { loadedTransaction ->
                    val selectedCategory =
                        _uiState.value.categories.find { it.name == loadedTransaction.category.name }
                    val formattedDate = formatISO8601ToDate(loadedTransaction.transactionDate)
                    val formattedTime = formatISO8601ToTime(loadedTransaction.transactionDate)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedCategory = selectedCategory,
                            currency = loadedTransaction.account.currency,
                            accountName = loadedTransaction.account.name,
                            categoryName = selectedCategory!!.name,
                            amount = loadedTransaction.amount,
                            expenseDate = formattedDate,
                            expenseTime = formattedTime,
                            comment = loadedTransaction.comment,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                        return@launch
                    }
                }
        }
    }

    fun validateAndUpdateTransaction(
        expenseId: Int,
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
            val domainModelTransaction = CreateTransactionDomainModel(
                accountId = ACCOUNT_ID,
                categoryId = _uiState.value.selectedCategory!!.id,
                amount = _uiState.value.amount,
                transactionDate = dateISOFormatted,
                comment = _uiState.value.comment
            )
            updateTransactionUseCase(
                transactionId = expenseId,
                transaction = domainModelTransaction
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

    fun deleteTransaction(expenseId: Int) {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionId = expenseId)
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

class ExpensesEditScreenViewModelFactory @Inject constructor(
    private val expensesEditScreenViewModelProvider: Provider<ExpensesEditScreenViewModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return expensesEditScreenViewModelProvider.get() as T
    }
}