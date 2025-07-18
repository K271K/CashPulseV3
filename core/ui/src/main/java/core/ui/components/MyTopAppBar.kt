package core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * TopAppBar без использования Material3
 */
@Composable
fun MyTopAppBar(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onLeadingIconClick: ()-> Unit = {},
    onTrailingIconClick: ()-> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.Companion
                .windowInsetsPadding(
                    WindowInsets.systemBars
                        .only(WindowInsetsSides.Companion.Top + WindowInsetsSides.Companion.Horizontal)
                )
                .height(64.dp)
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                TopAppBarIcon(
                    iconId = leadingIcon,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = {
                                onLeadingIconClick.invoke()
                            },
                        )
                )
            } ?: Spacer(modifier = Modifier.size(48.dp))

            Text(
                text = text,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                softWrap = false,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge
            )

            trailingIcon?.let {
                TopAppBarIcon(
                    iconId = trailingIcon,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = {
                                onTrailingIconClick.invoke()
                            },
                        )
                )
            } ?: Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
private fun TopAppBarIcon(
    @DrawableRes iconId: Int,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = tint
        )
    }
}