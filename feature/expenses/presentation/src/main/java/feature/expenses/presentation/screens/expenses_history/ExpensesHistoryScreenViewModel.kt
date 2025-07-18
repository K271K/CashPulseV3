package feature.expenses.presentation.screens.expenses_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.utils.formatCurrencyFromTextToSymbol
import core.domain.utils.formatDateFromLongToHuman
import feature.expenses.domain.usecase.GetPeriodExpensesUseCase
import feature.expenses.presentation.model.toExpenseUiModelList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Provider

class ExpensesHistoryScreenViewModel @Inject constructor(
    private val getPeriodExpensesUseCase: GetPeriodExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesHistoryScreenState(isLoading = true))
    val uiState: StateFlow<ExpensesHistoryScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val firstDayOfMonth = LocalDate.now().withDayOfMonth(1)
            val formattedFirstDayOfMonth = firstDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val todayDate = formatDateFromLongToHuman(System.currentTimeMillis())
            getPeriodExpensesUseCase(
                startDate = null,
                endDate = todayDate
            )
                .onSuccess { expensesList->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            expensesList = expensesList.toExpenseUiModelList(),
                            currency = formatCurrencyFromTextToSymbol(expensesList.first().account.currency),
                            endDate = todayDate,
                            startDate = formattedFirstDayOfMonth,
                            totalAmount = expensesList.sumOf { expense ->
                                expense.amount.toDouble()
                            }.toString()
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

    fun updateStartDate(dateInMillis: Long) {
        val formattedDate = formatDateFromLongToHuman(date = dateInMillis)
        _uiState.update {
            it.copy(startDate = formattedDate)
        }
    }

    fun updateEndDate(dateInMillis: Long) {
        val formattedDate = formatDateFromLongToHuman(date = dateInMillis)
        _uiState.update {
            it.copy(endDate = formattedDate)
        }
    }
}

class ExpensesHistoryScreenViewModelFactory @Inject constructor(
    private val expensesHistoryScreenViewModelProvider: Provider<ExpensesHistoryScreenViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return expensesHistoryScreenViewModelProvider.get() as T
    }

}