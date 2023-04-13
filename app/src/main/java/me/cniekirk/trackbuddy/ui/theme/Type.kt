package me.cniekirk.trackbuddy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.cniekirk.trackbuddy.R

val LexenDecaFamily = FontFamily(
    Font(R.font.lexenddeca_regular),
    Font(R.font.lexenddeca_bold, FontWeight.Bold),
    Font(R.font.lexenddeca_black, FontWeight.Black),
    Font(R.font.lexenddeca_light, FontWeight.Light),
    Font(R.font.lexenddeca_thin, FontWeight.Thin),
    Font(R.font.lexenddeca_semibold, FontWeight.SemiBold),
    Font(R.font.lexenddeca_extrabold, FontWeight.ExtraBold),
    Font(R.font.lexenddeca_extralight, FontWeight.ExtraLight),
    Font(R.font.lexenddeca_medium, FontWeight.Medium)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = LexenDecaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
)