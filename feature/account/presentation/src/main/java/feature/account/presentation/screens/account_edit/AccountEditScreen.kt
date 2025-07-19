package feature.account.presentation.screens.account_edit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.R
import core.ui.components.MyErrorBox
import core.ui.components.MyListItemOnlyText
import core.ui.components.MyLoadingIndicator
import core.ui.components.MyTopAppBar
import feature.account.presentation.model.Currency

@Composable
fun AccountEditScreen(
    accountId: Int,
    accountEditScreenViewModelFactory: AccountEditScreenViewModelFactory,
    goBack: () -> Unit,
) {

    val accountEditScreenViewModel: AccountEditScreenViewModel = viewModel(
        factory = accountEditScreenViewModelFactory
    )
    val uiState by accountEditScreenViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(accountId) {
        accountEditScreenViewModel.initWithAccountId(
            accountId = accountId
        )
    }

    BackHandler {
        goBack()
    }

    val context = LocalContext.current

    AccountScreenContent(
        uiState = uiState,
        onNameChanged = remember { accountEditScreenViewModel::updateAccountName },
        onBalanceChanged = remember { accountEditScreenViewModel::updateAccountBalance },
        onCurrencyChanged = remember { accountEditScreenViewModel::updateAccountCurrency },
        goBack = goBack,
        onUpdateAccount = remember {
            {
                accountEditScreenViewModel.validateAndUpdateAccount(
                    onValidationError = { errorMessage->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreenContent(
    uiState: AccountEditScreenState,
    onNameChanged: (String) -> Unit,
    onBalanceChanged: (String) -> Unit,
    onCurrencyChanged: (String) -> Unit,
    goBack: () -> Unit,
    onUpdateAccount: ()->Unit,
) {
    var showCurrencySheet by remember { mutableStateOf(false) }
    when {
        uiState.success -> {
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
                            goBack()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "Назад")
                    }
                }
            }
        }
        uiState.errorMessage != null -> {
            MyErrorBox(
                message = "${uiState.errorMessage}",
                modifier = Modifier.fillMaxSize()
            )
        }
        uiState.isLoading -> {
            MyLoadingIndicator()
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                MyTopAppBar(
                    text = "Редактирование счёта",
                    leadingIcon = R.drawable.cross,
                    trailingIcon = R.drawable.check,
                    onLeadingIconClick = {
                        goBack()
                    },
                    onTrailingIconClick = {
                        onUpdateAccount()
                    },
                )
                MyListItemOnlyText(
                    modifier = Modifier
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    content = {
                        Text(text = "Название счёта")
                    },
                    trailContent = {
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = uiState.name,
                            onValueChange = { onNameChanged(it) },
                            colors = TextFieldDefaults.colors().copy(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.End
                            )
                        )
                    }
                )
                HorizontalDivider()
                MyListItemOnlyText(
                    modifier = Modifier
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    content = {
                        Text(text = "Баланс")
                    },
                    trailContent = {
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = uiState.balance,
                            onValueChange = { onBalanceChanged(it) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors().copy(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.End
                            ),
                        )
                    }
                )
                HorizontalDivider()
                MyListItemOnlyText(
                    modifier = Modifier
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    content = {
                        Text(text = "Валюта")
                    },
                    trailContent = {
                        Text(
                            text = uiState.currency,
                            modifier = Modifier.padding(end = 16.dp),
                        )
                    },
                    onClick = { showCurrencySheet = true }
                )
                if (showCurrencySheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showCurrencySheet = false }
                    ) {
                        Currency.values().forEach {
                            ListItem(
                                modifier = Modifier.clickable {
                                    onCurrencyChanged(it.name)
                                    showCurrencySheet = false
                                },
                                headlineContent = { Text(it.displayName) }
                            )
                        }
                    }
                }
            }
        }
    }
}