package ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun ViewModeSelector(
    currentViewMode: ViewMode,
    setViewMode: (ViewMode) -> Unit,
) {
    val animationState = remember { MutableTransitionState(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.padding(horizontal = 4.dp).zIndex(0f),
            onClick = { animationState.targetState = !animationState.currentState }
        ) {
            Icon(imageVector = Icons.Default.Menu, null)
        }

        AnimatedVisibility(
            visibleState = animationState,
            modifier = Modifier.zIndex(1f),
            enter = slideIn(initialOffset = { IntOffset(-100, 0) }) + fadeIn(),
            exit = slideOut(targetOffset = { IntOffset(-100, 0) }) + fadeOut(),
        ) {
            Row {
                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { setViewMode(ViewMode.CPU); animationState.targetState = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    ),
                ) { Text("CPU") }

                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { setViewMode(ViewMode.GPU); animationState.targetState = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    ),
                ) { Text("GPU") }

            }
        }

        if (animationState.isIdle && !animationState.currentState) {
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = currentViewMode.name,
            )
        }
    }
}