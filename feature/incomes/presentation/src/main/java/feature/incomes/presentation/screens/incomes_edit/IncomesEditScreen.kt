package feature.incomes.presentation.screens.incomes_edit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.R
import core.ui.components.CategoryPickerDialog
import core.ui.components.DatePickerDialogComponent
import core.ui.components.MyErrorBox
import core.ui.components.MyListItemOnlyText
import core.ui.components.MyLoadingIndicator
import core.ui.components.MyPickerRow
import core.ui.components.MyTopAppBar
import core.ui.components.TimePickerDialogComponent
import core.ui.model.CategoryPickerUiModel
import feature.incomes.presentation.screens.EditIncomeScreenState

@Composable
fun IncomesEditScreen(
    expenseId: Int,
    incomesEditScreenViewModelFactory: IncomesEditScreenViewModelFactory,
    goBack: () -> Unit,
) {
    val incomesEditScreenViewModel: IncomesEditScreenViewModel = viewModel(
        factory = incomesEditScreenViewModelFactory,
    )
    val uiState by incomesEditScreenViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(expenseId) {
        incomesEditScreenViewModel.initWithId(expenseId = expenseId)
    }

    BackHandler {
        goBack()
    }

    val context = LocalContext.current

    IncomesEditScreenContent(
        uiState = uiState,
        onCancelClick = goBack,
        onAmountChange = remember { incomesEditScreenViewModel::updateAmount },
        onDateChange = remember { incomesEditScreenViewModel::updateDate },
        onCategoryChange = remember { incomesEditScreenViewModel::updateCategory },
        onTimeChange = remember { incomesEditScreenViewModel::updateTime },
        onCommentChange = remember { incomesEditScreenViewModel::updateComment },
        onUpdateTransactionClick = remember {
            {
                incomesEditScreenViewModel.validateAndUpdateTransaction(
                    expenseId = expenseId,
                    onValidationError = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        },
        onDeleteTransactionClick = remember {
            { incomesEditScreenViewModel.deleteTransaction(expenseId = expenseId) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomesEditScreenContent(
    uiState: EditIncomeScreenState,
    onCancelClick: () -> Unit,
    afterSuccessUpdated: () -> Unit = onCancelClick,
    onAmountChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onCategoryChange: (CategoryPickerUiModel) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onUpdateTransactionClick: () -> Unit,
    onDeleteTransactionClick: () -> Unit
) {
    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()

    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }
    val timePickerState = rememberTimePickerState()

    var showCategoryPickerDialog by remember {
        mutableStateOf(false)
    }
    if (uiState.success) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Операция прошла успешно",
                )
                TextButton(
                    onClick = {
                        afterSuccessUpdated()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Назад")
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MyTopAppBar(
                text = "Мои расходы",
                leadingIcon = R.drawable.cross,
                onLeadingIconClick = {
                    onCancelClick()
                },
                trailingIcon = R.drawable.check,
                onTrailingIconClick = {
                    onUpdateTransactionClick()
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
                            onUpdateTransactionClick()
                        }
                    )
                }

                else -> {
                    MyListItemOnlyText(
                        modifier = Modifier
                            .height(70.dp),
                        content = {
                            Text(
                                text = "Счёт"
                            )
                        },
                        trailContent = {
                            Text(
                                text = uiState.accountName
                            )
                            Icon(
                                painter = painterResource(R.drawable.more_right),
                                contentDescription = "Bank account name"
                            )
                        }
                    )
                    HorizontalDivider()
                    MyPickerRow(
                        modifier = Modifier
                            .height(70.dp),
                        leadingText = "Статья",
                        trailingText = uiState.categoryName,
                        trailIcon = {
                            Icon(
                                painter = painterResource(R.drawable.more_right),
                                contentDescription = "Bank account name"
                            )
                        },
                        onClick = {
                            showCategoryPickerDialog = true
                        }
                    )
                    HorizontalDivider()
                    MyListItemOnlyText(
                        modifier = Modifier
                            .height(70.dp),
                        content = {
                            Text(
                                text = "Сумма"
                            )
                        },
                        trailContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = "${uiState.amount}",
                                    onValueChange = {
                                        onAmountChange(it)
                                    },
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        textAlign = TextAlign.End
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                )
                                Text(
                                    text = uiState.currency,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 4.dp) // Небольшой отступ от поля
                                )
                            }
                        }
                    )
                    HorizontalDivider()
                    MyPickerRow(
                        modifier = Modifier
                            .height(70.dp),
                        leadingText = "Дата",
                        trailingText = uiState.expenseDate,
                        onClick = {
                            showDatePickerDialog = true
                        }
                    )
                    HorizontalDivider()
                    MyPickerRow(
                        modifier = Modifier
                            .height(70.dp),
                        leadingText = "Время",
                        trailingText = uiState.expenseTime,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    HorizontalDivider()
                    MyListItemOnlyText(
                        modifier = Modifier
                            .height(70.dp),
                        content = {
                            Text(text = "Комментарий")
                        },
                        trailContent = {
                            BasicTextField(
                                value = uiState.comment,
                                onValueChange = {
                                    onCommentChange(it)
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    textAlign = TextAlign.End
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                            )
                        }
                    )
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            onDeleteTransactionClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = "Удалить расход")
                    }
                    CategoryPickerDialog(
                        categoriesList = uiState.categories,
                        showDialog = showCategoryPickerDialog,
                        onDismiss = {
                            showDatePickerDialog = false
                        },
                        onConfirm = { selectedCategory ->
                            onCategoryChange(selectedCategory)
                            showCategoryPickerDialog = false
                        }
                    )
                    DatePickerDialogComponent(
                        showDialog = showDatePickerDialog,
                        datePickerState = datePickerState,
                        onConfirm = {
                            val selectedDateInMillis = datePickerState.selectedDateMillis
                            selectedDateInMillis?.let {
                                onDateChange(it)
                            }
                            showDatePickerDialog = false
                        },
                        onDismiss = {
                            showDatePickerDialog = false
                        }
                    )
                    TimePickerDialogComponent(
                        showDialog = showTimePickerDialog,
                        timePickerState = timePickerState,
                        onDismiss = {
                            showTimePickerDialog = false
                        },
                        onConfirm = {
                            onTimeChange(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            showTimePickerDialog = false
                        },
                    )
                }
            }
        }
    }
}