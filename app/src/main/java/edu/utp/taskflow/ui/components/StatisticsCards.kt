package edu.utp.taskflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import edu.utp.taskflow.ui.theme.LocalStatusColors
import edu.utp.taskflow.ui.theme.Muted
import edu.utp.taskflow.ui.theme.SoftBorder

@Composable
fun StatisticsSection(total: Int, pending: Int, completed: Int) {
    val status = LocalStatusColors.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        StatisticCard(
            "Total", total, Icons.AutoMirrored.Filled.FormatListBulleted,
            MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer,
            Modifier.weight(1f)
        )
        StatisticCard(
            "Pendientes", pending, Icons.Filled.Schedule,
            status.warning, status.warningContainer,
            Modifier.weight(1f)
        )
        StatisticCard(
            "Hechas", completed, Icons.Filled.CheckCircle,
            status.success, status.successContainer,
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatisticCard(
    label: String,
    count: Int,
    icon: ImageVector,
    accent: Color,
    accentContainer: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .border(1.dp, SoftBorder, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier.size(32.dp).background(accentContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
            }
        }
        Text(text = label, color = Muted, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
    }
}
