package feature.incomes.presentation.screens.incomes_add

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
fun IncomesAddScreen(
    incomesAddScreenViewModelFactory: IncomesAddScreenViewModelFactory,
    goBack: () -> Unit
) {

    val incomesAddScreenViewModel : IncomesAddScreenViewModel = viewModel(
        factory = incomesAddScreenViewModelFactory
    )

    val uiState by incomesAddScreenViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        goBack()
    }

    val context = LocalContext.current

    /**
     * Тут используется remember, чтобы при каждй рекомпозиции
     * не создавалась новая лямбда
     */
    IncomesAddScreenContent(
        uiState = uiState,
        onCancelClick = goBack,
        onAmountChange = remember { incomesAddScreenViewModel::updateAmount },
        onDateChange = remember { incomesAddScreenViewModel::updateDate },
        onCategoryChange = remember { incomesAddScreenViewModel::updateCategory },
        onTimeChange = remember { incomesAddScreenViewModel::updateTime },
        onCommentChange = remember { incomesAddScreenViewModel::updateComment },
        onCreateTransactionClick = remember {
            {
                incomesAddScreenViewModel.validateAndCreateTransaction { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomesAddScreenContent(
    uiState: EditIncomeScreenState,
    onCancelClick: () -> Unit,
    afterSuccessCreated: () -> Unit = onCancelClick,
    onAmountChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onCategoryChange: (CategoryPickerUiModel) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onCreateTransactionClick: () -> Unit,
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
            modifier = Modifier.Companion
                .fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Text(
                    text = "Операция прошла успешно",
                )
                TextButton(
                    onClick = {
                        afterSuccessCreated()
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
            modifier = Modifier.Companion
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
                    onCreateTransactionClick()
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

                        }
                    )
                }

                else -> {
                    MyListItemOnlyText(
                        modifier = Modifier.Companion
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
                        modifier = Modifier.Companion
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
                        modifier = Modifier.Companion
                            .height(70.dp),
                        content = {
                            Text(
                                text = "Сумма"
                            )
                        },
                        trailContent = {
                            Row(
                                verticalAlignment = Alignment.Companion.CenterVertically
                            ) {
                                BasicTextField(
                                    value = "${uiState.amount}",
                                    onValueChange = {
                                        onAmountChange(it)
                                    },
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        textAlign = TextAlign.Companion.End
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Companion.Number,
                                        imeAction = ImeAction.Companion.Next
                                    ),
                                    singleLine = true,
                                )
                                Text(
                                    text = uiState.currency,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.Companion.padding(start = 4.dp) // Небольшой отступ от поля
                                )
                            }
                        }
                    )
                    HorizontalDivider()
                    MyPickerRow(
                        modifier = Modifier.Companion
                            .height(70.dp),
                        leadingText = "Дата",
                        trailingText = uiState.expenseDate,
                        onClick = {
                            showDatePickerDialog = true
                        }
                    )
                    HorizontalDivider()
                    MyPickerRow(
                        modifier = Modifier.Companion
                            .height(70.dp),
                        leadingText = "Время",
                        trailingText = uiState.expenseTime,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    HorizontalDivider()
                    MyListItemOnlyText(
                        modifier = Modifier.Companion
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
                                    textAlign = TextAlign.Companion.End
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Companion.Next
                                ),
                                singleLine = true,
                            )
                        }
                    )
                    HorizontalDivider()
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