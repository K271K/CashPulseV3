package feature.incomes.presentation.screens.incomes_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.utils.formatCurrencyFromTextToSymbol
import core.domain.utils.formatDateFromLongToHuman
import feature.incomes.domain.usecase.GetPeriodIncomesUseCase
import feature.incomes.presentation.model.toIncomeUiModelList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Provider

class IncomesHistoryScreenViewModel @Inject constructor(
    private val getPeriodIncomesUseCase: GetPeriodIncomesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomesHistoryScreenState(isLoading = true))
    val uiState: StateFlow<IncomesHistoryScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val firstDayOfMonth = LocalDate.now().withDayOfMonth(1)
            val formattedFirstDayOfMonth = firstDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val todayDate = formatDateFromLongToHuman(System.currentTimeMillis())
            getPeriodIncomesUseCase(
                startDate = formattedFirstDayOfMonth,
                endDate = todayDate
            )
                .onSuccess { expensesList->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            expensesList = expensesList.toIncomeUiModelList(),
                            currency = if (expensesList.isNotEmpty()) formatCurrencyFromTextToSymbol(
                                expensesList.first().account.currency
                            ) else "",
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
            it.copy(
                isLoading = true,
                startDate = formattedDate
            )
        }
        viewModelScope.launch {
            getPeriodIncomesUseCase(
                startDate = formattedDate,
                endDate = _uiState.value.endDate
            )
                .onSuccess { expensesList->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            expensesList = expensesList.toIncomeUiModelList(),
                            currency = if (expensesList.isNotEmpty()) formatCurrencyFromTextToSymbol(
                                expensesList.first().account.currency
                            ) else it.currency,
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

    fun updateEndDate(dateInMillis: Long) {
        val formattedDate = formatDateFromLongToHuman(date = dateInMillis)
        _uiState.update {
            it.copy(
                isLoading = true,
                endDate = formattedDate
            )
        }
        viewModelScope.launch {
            getPeriodIncomesUseCase(
                startDate = _uiState.value.startDate,
                endDate = formattedDate
            )
                .onSuccess { expensesList->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            expensesList = expensesList.toIncomeUiModelList(),
                            currency = if (expensesList.isNotEmpty()) formatCurrencyFromTextToSymbol(
                                expensesList.first().account.currency
                            ) else it.currency,
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
}

class IncomesHistoryScreenViewModelFactory @Inject constructor(
    private val incomesHistoryScreenViewModelProvider: Provider<IncomesHistoryScreenViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return incomesHistoryScreenViewModelProvider.get() as T
    }

}