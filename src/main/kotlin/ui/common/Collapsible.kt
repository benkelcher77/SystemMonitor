package ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

sealed interface CollapsibleState {
    object Collapsed : CollapsibleState
    object Shown : CollapsibleState
}

@Composable
fun ColumnScope.Collapsible(
    modifier: Modifier = Modifier,
    weight: Float = 1f,
    state: CollapsibleState,
    onStateChanged: (CollapsibleState) -> Unit,
    collapsedContent: @Composable (state: CollapsibleState) -> Unit,
    fullContent: @Composable () -> Unit,
) {
    val shownModifier =
        if (weight == 0f)
            modifier
        else
            modifier.weight(weight)

    when (state) {
        CollapsibleState.Shown ->
            Column(
                modifier = shownModifier
            ) {
                Box(
                    modifier = Modifier.clickable { onStateChanged(CollapsibleState.Collapsed) }
                ) {
                    collapsedContent(state)
                }
                fullContent()
            }
        CollapsibleState.Collapsed -> {
            Box(
                modifier = modifier.clickable { onStateChanged(CollapsibleState.Shown) }
            ) {
                collapsedContent(state)
            }
        }
    }
}