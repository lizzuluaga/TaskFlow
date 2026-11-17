package edu.utp.taskflow.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.utp.taskflow.model.Task
import edu.utp.taskflow.model.TaskPriority
import edu.utp.taskflow.ui.theme.LocalStatusColors
import edu.utp.taskflow.ui.theme.Muted
import edu.utp.taskflow.ui.theme.SoftBorder

@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    readOnly: Boolean = false
) {
    val (priorityColor, _) = priorityColors(task.priority)
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, SoftBorder, shape)
            .clickable(enabled = !readOnly, onClick = onClick)
    ) {
        // Franja de color según la prioridad
        Box(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(if (task.isCompleted) SoftBorder else priorityColor)
        )

        Row(
            modifier = Modifier.weight(1f).padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoundCheck(
                checked = task.isCompleted,
                enabled = !readOnly,
                onToggle = onToggle
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) Muted else MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PriorityBadge(task.priority)
                    Text("· ${task.category}", style = MaterialTheme.typography.bodyMedium, color = Muted)
                }
            }

            if (!readOnly) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = "Eliminar tarea", tint = Muted)
                }
            }
        }
    }
}

/** Casilla circular que se rellena al completar la tarea. */
@Composable
private fun RoundCheck(checked: Boolean, enabled: Boolean, onToggle: () -> Unit) {
    val success = LocalStatusColors.current.success
    val fill by animateColorAsState(
        if (checked) success else Color.Transparent, label = "check"
    )
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(fill)
            .border(2.dp, if (checked) success else Muted, CircleShape)
            .clickable(enabled = enabled, onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Completada",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun priorityColors(priority: TaskPriority): Pair<Color, Color> {
    val status = LocalStatusColors.current
    return when (priority) {
        TaskPriority.HIGH -> status.danger to status.dangerContainer
        TaskPriority.MEDIUM -> status.warning to status.warningContainer
        TaskPriority.LOW -> status.success to status.successContainer
    }
}

@Composable
fun PriorityBadge(priority: TaskPriority) {
    val label = when (priority) {
        TaskPriority.HIGH -> "Alta"
        TaskPriority.MEDIUM -> "Media"
        TaskPriority.LOW -> "Baja"
    }
    val (tint, background) = priorityColors(priority)
    Row(
        modifier = Modifier
            .background(background, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(Modifier.size(6.dp).background(tint, CircleShape))
        Text(label, color = tint, style = MaterialTheme.typography.labelLarge)
    }
}
