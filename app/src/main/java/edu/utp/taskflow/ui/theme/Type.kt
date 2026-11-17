package edu.utp.taskflow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Rounded = FontFamily.SansSerif

val TaskTypography = Typography(
    displaySmall = TextStyle(fontFamily = Rounded, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 40.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontFamily = Rounded, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 34.sp, letterSpacing = (-0.4).sp),
    headlineMedium = TextStyle(fontFamily = Rounded, fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp),
    headlineSmall = TextStyle(fontFamily = Rounded, fontSize = 22.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontFamily = Rounded, fontSize = 19.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontFamily = Rounded, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = Rounded, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = Rounded, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = Rounded, fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    labelSmall = TextStyle(fontFamily = Rounded, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
)
