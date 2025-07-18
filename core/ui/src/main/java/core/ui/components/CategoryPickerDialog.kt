package core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.ui.R
import core.ui.model.CategoryPickerUiModel

@Composable
fun CategoryPickerDialog(
    categoriesList: List<CategoryPickerUiModel>,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (CategoryPickerUiModel) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(
                    text = "Выберите категорию"
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = categoriesList,
                        key = {
                            it.id
                        }
                    ) {
                        HorizontalDivider()
                        MyListItemWithLeadIcon(
                            modifier = Modifier
                                .height(56.dp),
                            icon = it.emoji,
                            iconBg = MaterialTheme.colorScheme.secondary,
                            content = {
                                Text(
                                    text = it.name
                                )
                            },
                            trailContent = {
                                Icon(
                                    painter = painterResource(R.drawable.more_right),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onConfirm(it)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {

            }
        )
    }
}