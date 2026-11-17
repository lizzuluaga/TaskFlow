package edu.utp.taskflow.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Tarjeta de progreso con degradado y un anillo circular animado.
 */
@Composable
fun ProgressCard(progress: Float, completed: Int, total: Int) {
    val animated by animateFloatAsState(targetValue = progress, label = "progress")
    val onColor = MaterialTheme.colorScheme.onPrimary

    val message = when {
        total == 0 -> "Agrega tu primera tarea para empezar."
        progress >= 1f -> "¡Todo listo! Terminaste todas tus tareas."
        progress >= 0.5f -> "Vas muy bien, ya pasaste la mitad."
        else -> "Un paso a la vez, tú puedes."
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
            .padding(22.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "PROGRESO DE HOY",
                    color = onColor.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    "$completed de $total tareas",
                    color = onColor,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    message,
                    color = onColor.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Anillo de progreso
            Box(modifier = Modifier.size(84.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(84.dp)) {
                    val stroke = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawArc(
                        color = onColor.copy(alpha = 0.25f),
                        startAngle = 0f, sweepAngle = 360f,
                        useCenter = false, style = stroke
                    )
                    drawArc(
                        color = onColor,
                        startAngle = -90f, sweepAngle = 360f * animated,
                        useCenter = false, style = stroke
                    )
                }
                Text(
                    "${(progress * 100).toInt()}%",
                    color = onColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
