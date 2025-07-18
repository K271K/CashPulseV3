package core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogComponent(
    showDialog: Boolean,
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
    onConfirm: (TimePickerState) -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Отмена")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(timePickerState)
                        onDismiss()
                    }
                ) {
                    Text(text = "Ок")
                }
            },
            text = {
                TimePicker(
                    state = timePickerState
                )
            }
        )
    }

}