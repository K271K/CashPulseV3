package core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Обёртка над ListItem для ситуаций, когда в строке в начале идёт иконка
 * Пример: расходы, категории
 */
@Composable
fun MyListItemWithLeadIcon (
    modifier: Modifier = Modifier,
    icon: String,
    iconBg: Color,
    content: @Composable () -> Unit,
    trailContent: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    MyListItem(
        modifier = modifier,
        leadContent = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconBg)
            ) {
                Text(text = icon, modifier = Modifier.align(Alignment.Center))
            }
        },
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
private fun MyListItemWithLeadIconPreview() {
    MyListItemWithLeadIcon(
        modifier = Modifier
            .height(56.dp),
        icon = "\uD83D\uDCBB",
        iconBg = MaterialTheme.colorScheme.secondary,
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
