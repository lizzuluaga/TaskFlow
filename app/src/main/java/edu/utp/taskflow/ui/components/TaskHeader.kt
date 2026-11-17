package edu.utp.taskflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.utp.taskflow.ui.theme.Muted
import edu.utp.taskflow.ui.theme.TaskFlowPalette
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TaskHeader(
    palette: TaskFlowPalette,
    onPaletteChange: (TaskFlowPalette) -> Unit,
    username: String? = null,
    onLogout: () -> Unit = {}
) {
    val greeting = when (LocalTime.now().hour) {
        in 5..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }
    val today = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale.forLanguageTag("es")))
        .replaceFirstChar { it.uppercase() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar con la inicial del usuario
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                (username?.firstOrNull() ?: '?').uppercase(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(today, color = Muted, style = MaterialTheme.typography.bodyMedium)
            Text(
                if (username != null) "$greeting, $username" else greeting,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Cambiar paleta de colores
        FilledTonalIconButton(onClick = {
            onPaletteChange(
                if (palette == TaskFlowPalette.LAVANDA) TaskFlowPalette.MENTA
                else TaskFlowPalette.LAVANDA
            )
        }) {
            Icon(Icons.Filled.Palette, contentDescription = "Cambiar a ${
                if (palette == TaskFlowPalette.LAVANDA) TaskFlowPalette.MENTA.label
                else TaskFlowPalette.LAVANDA.label
            }")
        }

        // Cerrar sesión: vuelve a la pantalla de login
        FilledTonalIconButton(onClick = onLogout) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
        }
    }
}
