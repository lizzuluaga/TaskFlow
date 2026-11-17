package edu.utp.taskflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class TaskFlowPalette(val label: String) {
    LAVANDA("Lavanda"),
    MENTA("Menta")
}

// ───────── Esquemas de color: 2 paletas × (día / noche) ─────────

private val LavenderLight = lightColorScheme(
    primary = LavenderPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = LavenderContainerLight,
    onPrimaryContainer = LavenderOnContainerLight,
    tertiary = LavenderAccentLight,
    onTertiary = Color.White,
    background = LavenderBackgroundLight,
    onBackground = InkLight,
    surface = Color.White,
    onSurface = InkLight,
    surfaceVariant = LavenderSurfaceVariantLight,
    onSurfaceVariant = MutedLight,
    outline = MutedLight,
    outlineVariant = BorderLight
)

private val LavenderDark = darkColorScheme(
    primary = LavenderPrimaryDark,
    onPrimary = LavenderOnPrimaryDark,
    primaryContainer = LavenderContainerDark,
    onPrimaryContainer = LavenderOnContainerDark,
    tertiary = LavenderAccentDark,
    onTertiary = LavenderOnPrimaryDark,
    background = LavenderBackgroundDark,
    onBackground = InkDark,
    surface = LavenderSurfaceDark,
    onSurface = InkDark,
    surfaceVariant = LavenderSurfaceVariantDark,
    onSurfaceVariant = MutedDark,
    surfaceContainerLow = LavenderSurfaceDark,
    outline = MutedDark,
    outlineVariant = BorderDark
)

private val MintLight = lightColorScheme(
    primary = MintPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = MintContainerLight,
    onPrimaryContainer = MintOnContainerLight,
    tertiary = MintAccentLight,
    onTertiary = Color.White,
    background = MintBackgroundLight,
    onBackground = InkLight,
    surface = Color.White,
    onSurface = InkLight,
    surfaceVariant = MintSurfaceVariantLight,
    onSurfaceVariant = MutedLight,
    outline = MutedLight,
    outlineVariant = BorderLight
)

private val MintDark = darkColorScheme(
    primary = MintPrimaryDark,
    onPrimary = MintOnPrimaryDark,
    primaryContainer = MintContainerDark,
    onPrimaryContainer = MintOnContainerDark,
    tertiary = MintAccentDark,
    onTertiary = MintOnPrimaryDark,
    background = MintBackgroundDark,
    onBackground = InkDark,
    surface = MintSurfaceDark,
    onSurface = InkDark,
    surfaceVariant = MintSurfaceVariantDark,
    onSurfaceVariant = MutedDark,
    surfaceContainerLow = MintSurfaceDark,
    outline = MutedDark,
    outlineVariant = BorderDark
)

// ───────── Colores de estado (prioridades, completadas…) ─────────

@Immutable
data class StatusColors(
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val danger: Color,
    val dangerContainer: Color
)

private val LightStatus = StatusColors(
    success = Color(0xFF0F7A55),
    successContainer = Color(0xFFD7F5E7),
    warning = Color(0xFFA85F00),
    warningContainer = Color(0xFFFFEDD1),
    danger = Color(0xFFC0263A),
    dangerContainer = Color(0xFFFFE0E4)
)

private val DarkStatus = StatusColors(
    success = Color(0xFF6EE0AE),
    successContainer = Color(0xFF123D2C),
    warning = Color(0xFFFFC266),
    warningContainer = Color(0xFF45300F),
    danger = Color(0xFFFF8A98),
    dangerContainer = Color(0xFF4A1820)
)

val LocalStatusColors = staticCompositionLocalOf { LightStatus }

/**
 * Tema de la app.
 * [darkTheme] por defecto sigue la configuración del teléfono
 * (Ajustes → Pantalla → Tema oscuro).
 */
@Composable
fun TaskFlowTheme(
    palette: TaskFlowPalette = TaskFlowPalette.LAVANDA,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (palette) {
        TaskFlowPalette.LAVANDA -> if (darkTheme) LavenderDark else LavenderLight
        TaskFlowPalette.MENTA -> if (darkTheme) MintDark else MintLight
    }

    CompositionLocalProvider(
        LocalStatusColors provides if (darkTheme) DarkStatus else LightStatus
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TaskTypography,
            content = content
        )
    }
}
