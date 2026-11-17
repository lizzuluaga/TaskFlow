package edu.utp.taskflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// ───────── Paleta Lavanda (violeta + rosa) ─────────
val LavenderPrimaryLight = Color(0xFF5B4FE0)
val LavenderContainerLight = Color(0xFFE6E3FF)
val LavenderOnContainerLight = Color(0xFF1D1660)
val LavenderAccentLight = Color(0xFFD9468F)
val LavenderBackgroundLight = Color(0xFFF7F6FB)
val LavenderSurfaceVariantLight = Color(0xFFEEECF6)

val LavenderPrimaryDark = Color(0xFFB9B2FF)
val LavenderOnPrimaryDark = Color(0xFF231A7A)
val LavenderContainerDark = Color(0xFF3A2FA8)
val LavenderOnContainerDark = Color(0xFFE6E3FF)
val LavenderAccentDark = Color(0xFFFF9CC9)
val LavenderBackgroundDark = Color(0xFF121119)
val LavenderSurfaceDark = Color(0xFF1C1B25)
val LavenderSurfaceVariantDark = Color(0xFF2A2836)

// ───────── Paleta Menta (verde esmeralda + azul) ─────────
val MintPrimaryLight = Color(0xFF0F8A6A)
val MintContainerLight = Color(0xFFD2F4E8)
val MintOnContainerLight = Color(0xFF003828)
val MintAccentLight = Color(0xFF2F6FED)
val MintBackgroundLight = Color(0xFFF4FAF7)
val MintSurfaceVariantLight = Color(0xFFE6F2EC)

val MintPrimaryDark = Color(0xFF5FDDB4)
val MintOnPrimaryDark = Color(0xFF003828)
val MintContainerDark = Color(0xFF0B5A45)
val MintOnContainerDark = Color(0xFFD2F4E8)
val MintAccentDark = Color(0xFF8AB8FF)
val MintBackgroundDark = Color(0xFF0E1513)
val MintSurfaceDark = Color(0xFF17201C)
val MintSurfaceVariantDark = Color(0xFF23302B)

// ───────── Neutros ─────────
val InkLight = Color(0xFF1C1B26)
val MutedLight = Color(0xFF6B6880)
val BorderLight = Color(0xFFE2E0EC)

val InkDark = Color(0xFFECEAF4)
val MutedDark = Color(0xFFA9A6BC)
val BorderDark = Color(0xFF34323F)

// ───────── Colores que dependen del tema ─────────
// Se leen del tema actual, así cambian solos entre modo día y noche.

val Muted: Color
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onSurfaceVariant

val SoftBorder: Color
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.outlineVariant

val Success: Color
    @Composable @ReadOnlyComposable
    get() = LocalStatusColors.current.success
