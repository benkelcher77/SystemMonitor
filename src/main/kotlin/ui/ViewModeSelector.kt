package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ViewModeSelector(
    currentViewMode: ViewMode,
    setViewMode: (ViewMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        ViewMode.entries.forEach {
            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                onClick = { setViewMode(it) }
            ) {
                Text(it.name, color = if (it == currentViewMode) Color.Black else Color.Gray)
            }
        }
    }
}