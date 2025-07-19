package feature.incomes.presentation.screens.incomes_today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.R
import core.ui.components.MyErrorBox
import core.ui.components.MyFloatingActionButton
import core.ui.components.MyListItemOnlyText
import core.ui.components.MyListItemWithLeadIcon
import core.ui.components.MyLoadingIndicator
import core.ui.components.MyTextBox
import core.ui.components.MyTopAppBar

@Composable
fun IncomesTodayScreen(
    incomesTodayScreenViewModelFactory: IncomesTodayScreenViewModelFactory,
    goToHistoryScreen: ()-> Unit,
    goToAddIncomeScreen: () -> Unit,
    goToEditIncomeScreen: (Int) -> Unit
) {
    val incomesTodayScreenViewModel: IncomesTodayScreenViewModel = viewModel(
        factory = incomesTodayScreenViewModelFactory
    )
    val uiState by incomesTodayScreenViewModel.uiState.collectAsStateWithLifecycle()

    IncomesTodayScreenContent(
        uiState = uiState,
        goToHistoryScreen = goToHistoryScreen,
        goToAddTransactionScreen = goToAddIncomeScreen,
        goToEditIncomeScreen = goToEditIncomeScreen
    )
}

@Composable
private fun IncomesTodayScreenContent(
    uiState: IncomesTodayScreenState,
    goToHistoryScreen: ()-> Unit,
    goToAddTransactionScreen: () -> Unit,
    goToEditIncomeScreen: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            MyTopAppBar(
                text = "Расходы сегодня",
                trailingIcon = R.drawable.history,
                onTrailingIconClick = {
                    goToHistoryScreen()
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
                    HorizontalDivider()
                    if (uiState.expensesList.isEmpty()) {
                        MyTextBox(
                            message = "Нет доходов за сегодня",
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(
                                items = uiState.expensesList,
                                key = {it.id}
                            ) { expense ->
                                val onEditClick =
                                    remember(expense.id) { { goToEditIncomeScreen(expense.id) } }
                                MyListItemWithLeadIcon(
                                    modifier = Modifier
                                        .height(70.dp),
                                    icon = expense.categoryEmoji ?: "",
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
                                        Text(text = "${expense.amount} ${expense.currency}")
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
                }
            }
        }
        MyFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onClick = {
                goToAddTransactionScreen()
            }
        )
    }
}