package core.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Компонент для выбора даты, который используется на экранах истории транзакций
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogComponent(
    showDialog: Boolean,
    datePickerState: DatePickerState,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate ->
                            onConfirm(selectedDate)
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = "Ок")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Отмена")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}