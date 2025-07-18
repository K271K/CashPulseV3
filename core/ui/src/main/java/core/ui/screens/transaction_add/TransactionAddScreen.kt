package core.ui.screens.transaction_add

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TransactionAddScreen(isIncome: Boolean) {

    TransactionAddScreenContent(
        isIncome = isIncome
    )
}

@Composable
private fun TransactionAddScreenContent(isIncome: Boolean) {
    Text(text = isIncome.toString())
}