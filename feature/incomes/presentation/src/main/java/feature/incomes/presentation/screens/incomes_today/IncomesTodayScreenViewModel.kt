package feature.incomes.presentation.screens.incomes_today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import core.domain.utils.formatCurrencyFromTextToSymbol
import feature.incomes.domain.usecase.GetTodayIncomesUseCase
import feature.incomes.presentation.model.toIncomeUiModelList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class IncomesTodayScreenViewModel @Inject constructor(
    private val getTodayIncomesUseCase: GetTodayIncomesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomesTodayScreenState(isLoading = true))
    val uiState: StateFlow<IncomesTodayScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTodayIncomesUseCase()
                .onSuccess { expensesList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            expensesList = expensesList.toIncomeUiModelList(),
                            currency = if (expensesList.isNotEmpty()) formatCurrencyFromTextToSymbol(
                                expensesList.first().account.currency
                            ) else "",
                            totalAmount = expensesList.sumOf { expense -> expense.amount.toDouble()}.toString()
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

class IncomesTodayScreenViewModelFactory @Inject constructor(
    private val incomesTodayScreenViewModelProvider: Provider<IncomesTodayScreenViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return incomesTodayScreenViewModelProvider.get() as T
    }

}