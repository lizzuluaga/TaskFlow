package edu.utp.taskflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.utp.taskflow.model.TaskPriority
import edu.utp.taskflow.ui.theme.LocalStatusColors
import edu.utp.taskflow.ui.theme.Muted
import edu.utp.taskflow.ui.theme.SoftBorder

@Composable
fun NewTaskForm(
    title: String,
    onTitleChange: (String) -> Unit,
    selectedPriority: TaskPriority,
    onPriorityChange: (TaskPriority) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val status = LocalStatusColors.current

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Nueva tarea", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("¿Qué necesitas hacer?") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Prioridad", style = MaterialTheme.typography.titleMedium, color = Muted)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TaskPriority.entries.forEach { priority ->
                val (label, tint, container) = when (priority) {
                    TaskPriority.HIGH -> Triple("Alta", status.danger, status.dangerContainer)
                    TaskPriority.MEDIUM -> Triple("Media", status.warning, status.warningContainer)
                    TaskPriority.LOW -> Triple("Baja", status.success, status.successContainer)
                }
                val selected = selectedPriority == priority
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (selected) container else Color.Transparent)
                        .border(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) tint else SoftBorder,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onPriorityChange(priority) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (selected) tint else Muted, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Button(
            onClick = onSave,
            enabled = title.isNotBlank(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Guardar tarea") }

        TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
    }
}
