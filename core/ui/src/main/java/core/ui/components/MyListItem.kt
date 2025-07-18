package core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Базовый ListItem
 */
@Composable
fun MyListItem(
    modifier: Modifier = Modifier,
    leadContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit = {},
    trailContent: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = if (onClick == null) false else true,
                onClick = {
                    onClick?.invoke()
                }
            )
            .then(modifier)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadContent?.let {
            it()
            Spacer(modifier = Modifier.width(16.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        Spacer(modifier = Modifier.width(16.dp))
        trailContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun MyListItemPreview(){
    MyListItem(
        modifier = Modifier
            .height(56.dp)
            .background(MaterialTheme.colorScheme.secondary),
        leadContent = {
            Text(text = "leadContent")
        },
        content = {
            Text(text = "content")
        },
        trailContent = {
            Text(text = "trailContent")
        },
        onClick = {

        }
    )
}