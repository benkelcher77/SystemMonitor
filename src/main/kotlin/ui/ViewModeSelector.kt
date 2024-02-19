package ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
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
    var expanded by remember { mutableStateOf(false) }
    Row {
        IconButton(
            modifier = Modifier.padding(horizontal = 4.dp).zIndex(0f),
            onClick = { expanded = !expanded }
        ) {
            Icon(imageVector = Icons.Default.Menu, null)
        }

        AnimatedVisibility(
            modifier = Modifier.zIndex(1f),
            visible = expanded,
            enter = slideIn(initialOffset = { IntOffset(-100, 0) }) + fadeIn(),
            exit = slideOut(targetOffset = { IntOffset(-100, 0) }) + fadeOut(),
        ) {
            Row {
                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { setViewMode(ViewMode.CPU); expanded = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    ),
                ) { Text("CPU") }
                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { setViewMode(ViewMode.GPU); expanded = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    ),
                ) { Text("GPU") }
            }
        }
    }
}