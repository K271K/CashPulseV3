package core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
@Composable
fun MyListItemOnlyText(
    modifier: Modifier,
    content: @Composable () -> Unit,
    trailContent: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    MyListItem(
        modifier = modifier,
        leadContent = null,
        content = {
            content()
        },
        trailContent = {
            trailContent()
        },
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun MyListItemOnlyTextPreview() {
    MyListItemOnlyText(
        modifier = Modifier
            .height(56.dp),
        content = {
            Text(
                text = "Зарплата"
            )
        },
        trailContent = {
            Text(
                text = "100 000 R"
            )
        }
    )
}