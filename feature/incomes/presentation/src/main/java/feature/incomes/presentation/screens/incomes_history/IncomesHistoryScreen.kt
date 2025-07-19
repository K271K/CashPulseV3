package feature.incomes.presentation.screens.incomes_history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.R
import core.ui.components.DatePickerDialogComponent
import core.ui.components.MyErrorBox
import core.ui.components.MyListItemOnlyText
import core.ui.components.MyListItemWithLeadIcon
import core.ui.components.MyLoadingIndicator
import core.ui.components.MyPickerRow
import core.ui.components.MyTextBox
import core.ui.components.MyTopAppBar

@Composable
fun IncomesHistoryScreen(
    incomesHistoryScreenViewModelFactory: IncomesHistoryScreenViewModelFactory,
    goBack: () -> Unit,
    goToEditIncomeScreen: (Int) -> Unit
) {

    val incomesHistoryScreenViewModel: IncomesHistoryScreenViewModel = viewModel(
        factory = incomesHistoryScreenViewModelFactory
    )
    val uiState by incomesHistoryScreenViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        goBack()
    }

    IncomesHistoryScreenContent(
        uiState = uiState,
        goBack = goBack,
        onChooseStartDate = remember { incomesHistoryScreenViewModel::updateStartDate },
        onChooseEndDate = remember { incomesHistoryScreenViewModel::updateEndDate },
        goToEditIncomeScreen = goToEditIncomeScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomesHistoryScreenContent(
    uiState: IncomesHistoryScreenState,
    goBack: () -> Unit,
    onChooseStartDate: (Long) -> Unit,
    onChooseEndDate: (Long) -> Unit,
    goToEditIncomeScreen: (Int) -> Unit
) {
    var showStartDatePickerDialog by remember { mutableStateOf(false) }
    var showEndDatePickerDialog by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MyTopAppBar(
            text = "Моя история",
            leadingIcon = R.drawable.back,
            onLeadingIconClick = {
                goBack()
            },
            trailingIcon = R.drawable.clipboard_text,
            onTrailingIconClick = {

            }
        )
        when {
            uiState.isLoading -> {
                MyLoadingIndicator()
            }

            uiState.error != null -> {
                MyErrorBox(
                    message = uiState.error,
                    onRetryClick = {

                    },
                )
            }

            else -> {
                MyPickerRow(
                    leadingText = "Начало",
                    trailingText = uiState.startDate,
                    onClick = {
                        showStartDatePickerDialog = true
                    }
                )
                HorizontalDivider()
                MyPickerRow(
                    leadingText = "Конец",
                    trailingText = uiState.endDate,
                    onClick = {
                        showEndDatePickerDialog = true
                    }
                )
                HorizontalDivider()
                MyListItemOnlyText(
                    modifier = Modifier
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    content = {
                        Text(
                            text = "Сумма"
                        )
                    },
                    trailContent = {
                        Text("${uiState.totalAmount} ${uiState.currency}")
                    },
                )
                if (uiState.expensesList.isEmpty()) {
                    MyTextBox(
                        message = "Нет расходов за выбранный период\nПопробуйте выбрать другой период"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(
                            items = uiState.expensesList,
                            key = { it.id }
                        ) { expense ->
                            val onEditClick =
                                remember(expense.id) { { goToEditIncomeScreen(expense.id) } }
                            MyListItemWithLeadIcon(
                                modifier = Modifier
                                    .height(70.dp),
                                icon = expense.categoryEmoji ?: "💰",
                                iconBg = MaterialTheme.colorScheme.secondary,
                                content = {
                                    Column {
                                        Text(
                                            text = expense.categoryName
                                        )
                                        if (expense.comment.isNotEmpty()) {
                                            Text(
                                                text = expense.comment,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                },
                                trailContent = {
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(text = "${expense.amount} ${expense.currency}")
                                        Text(text = "${expense.date} ${expense.time}")
                                    }
                                    Icon(
                                        painter = painterResource(R.drawable.more_right),
                                        contentDescription = null,
                                    )
                                },
                                onClick = onEditClick
                            )
                            HorizontalDivider()
                        }
                    }
                }
                DatePickerDialogComponent(
                    showDialog = showStartDatePickerDialog,
                    datePickerState = startDatePickerState,
                    onDismiss = remember { { showStartDatePickerDialog = false } },
                    onConfirm = remember {
                        { selectedDate ->
                            onChooseStartDate(selectedDate)
                            showStartDatePickerDialog = false
                        }
                    }
                )
                DatePickerDialogComponent(
                    showDialog = showEndDatePickerDialog,
                    datePickerState = endDatePickerState,
                    onDismiss = remember { { showEndDatePickerDialog = false } },
                    onConfirm = remember {
                        { selectedDate ->
                            onChooseEndDate(selectedDate)
                            showEndDatePickerDialog = false
                        }
                    }
                )
            }
        }

    }
}