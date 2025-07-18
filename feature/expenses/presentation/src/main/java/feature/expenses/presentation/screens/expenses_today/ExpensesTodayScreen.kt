package feature.expenses.presentation.screens.expenses_today

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.R
import core.ui.components.MyErrorBox
import core.ui.components.MyListItemWithLeadIcon
import core.ui.components.MyLoadingIndicator
import core.ui.components.MyTopAppBar

@Composable
fun ExpensesTodayScreen(
    expensesTodayScreenViewModelFactory: ExpensesTodayScreenViewModelFactory,
    goToHistoryScreen: ()-> Unit,
    goToAddTransactionScreen: () -> Unit
) {
    val expensesTodayScreenViewModel: ExpensesTodayScreenViewModel = viewModel(
        factory = expensesTodayScreenViewModelFactory
    )
    val uiState by expensesTodayScreenViewModel.uiState.collectAsStateWithLifecycle()
    ExpensesTodayScreenContent(
        uiState = uiState,
        goToHistoryScreen = goToHistoryScreen
    )
}

@Composable
private fun ExpensesTodayScreenContent(
    uiState: ExpensesTodayScreenState,
    goToHistoryScreen: ()-> Unit,
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(
                            items = uiState.expensesList,
                            key = {it.id}
                        ) { expense ->
                            MyListItemWithLeadIcon(
                                modifier = Modifier
                                    .height(70.dp),
                                icon = expense.categoryEmoji,
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
                                onClick = {

                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }

}