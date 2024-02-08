package ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


@Composable
fun ColumnScope.Collapsible(
    modifier: Modifier = Modifier,
    weight: Float = 1f,
    collapsedContent: @Composable (open: Boolean) -> Unit,
    fullContent: @Composable () -> Unit,
) {
    var open by remember { mutableStateOf(true) }
    if (open) {
        Column(
            modifier = modifier.weight(weight)
        ) {
            Box(
                modifier = Modifier.clickable { open = false }
            ) {
                collapsedContent(open)
            }
            fullContent()
        }
    } else {
        Box(
            modifier = modifier.clickable { open = true }
        ) {
            collapsedContent(open)
        }
    }
}