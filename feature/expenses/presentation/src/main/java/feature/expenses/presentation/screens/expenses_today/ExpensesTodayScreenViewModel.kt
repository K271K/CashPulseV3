package feature.expenses.presentation.screens.expenses_today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import feature.expenses.domain.usecase.GetTodayExpensesUseCase
import feature.expenses.presentation.model.toExpenseUiModelList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ExpensesTodayScreenViewModel @Inject constructor(
    private val getTodayExpensesUseCase: GetTodayExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesTodayScreenState(isLoading = true))
    val uiState: StateFlow<ExpensesTodayScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTodayExpensesUseCase()
                .onSuccess { expensesList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            expensesList = expensesList.toExpenseUiModelList(),
                        )
                    }
                }
                .onFailure { error->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }
}

class ExpensesTodayScreenViewModelFactory @Inject constructor(
    private val expensesTodayScreenViewModelProvider: Provider<ExpensesTodayScreenViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return expensesTodayScreenViewModelProvider.get() as T
    }

}