package edu.utp.taskflow.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.utp.taskflow.model.TaskFilter
import edu.utp.taskflow.ui.theme.Muted

/**
 * Selector segmentado tipo "pastilla" para filtrar las tareas.
 */
@Composable
fun TaskFilterBar(selected: TaskFilter, onChange: (TaskFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        listOf(
            TaskFilter.ALL to "Todas",
            TaskFilter.PENDING to "Pendientes",
            TaskFilter.COMPLETED to "Hechas"
        ).forEach { (filter, label) ->
            val isSelected = selected == filter
            val bg by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                label = "filterBg"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bg)
                    .clickable { onChange(filter) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Muted
                )
            }
        }
    }
}
