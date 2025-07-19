package feature.account.presentation.screens.account_main_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.R
import core.ui.components.MyErrorBox
import core.ui.components.MyListItemOnlyText
import core.ui.components.MyLoadingIndicator
import core.ui.components.MyTextBox
import core.ui.components.MyTopAppBar

@Composable
fun AccountScreen(
    accountScreenViewModelFactory: AccountScreenViewModelFactory,
    goToEditAccount: (Int) -> Unit
) {
    val accountScreenViewModel: AccountScreenViewModel = viewModel(
        factory = accountScreenViewModelFactory
    )
    val uiState by accountScreenViewModel.uiState.collectAsStateWithLifecycle()
    AccountScreenContent(
        uiState = uiState,
        goToEditAccount = goToEditAccount
    )
}

@Composable
private fun AccountScreenContent(
    uiState: AccountScreenState,
    goToEditAccount: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    MyLoadingIndicator()
                }
                uiState.error != null -> {
                    MyErrorBox(
                        message = uiState.error
                    )
                }
                else -> {
                    MyTopAppBar(
                        text = "Мой счёт",
                        trailingIcon = R.drawable.edit,
                        onTrailingIconClick = {
                            //TODO переделать к чертям
                            goToEditAccount(uiState.accountsList.filter { it.isSelected }.first().id)
                        }
                    )
                    if (uiState.accountsList.isEmpty()) {
                        MyTextBox(
                            message = "У вас нет счетов. Пожалуйста создайте счёт"
                        )
                    } else {
                        LazyColumn {
                            items(
                                items = uiState.accountsList,
                                key = {it.id}
                            ) {
                                MyListItemOnlyText(
                                    modifier = Modifier
                                        .height(56.dp)
                                        .background(if (it.isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background),
                                    content = {
                                        Text(
                                            text = it.name
                                        )
                                    },
                                    trailContent = {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                                            contentDescription = null
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
}