package com.example.flixyConnect.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import com.example.flixyConnect.R

private val DarkColors = darkColorScheme(
    background = Color(0xFF121214),
    surface = Color(0xFF1C1C20),
    primary = Color(0xFFFF4B3E),
    onPrimary = Color.White,
    onSurface = Color(0xFFFFFFFF),
    outline = Color(0xFF2A2A30),
    secondary = Color(0xFF9CA3AF),
    tertiary = Color(0xFF1E1E20)
)

private val LightColors = lightColorScheme(
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    primary = Color(0xFFE50914),
    onPrimary = Color.White,
    onSurface = Color(0xFF000000),
    outline = Color(0xFFEBEBEB),
    secondary = Color(0xFF9AA4B2),
    tertiary = Color(0xFFF2F4F7)
)

val Montserrat = FontFamily(
    Font(R.font.montserrat, weight = FontWeight.Bold)
)
val Raleway = FontFamily(
    Font(R.font.raleway, weight = FontWeight.Medium)
)
val Roboto = FontFamily(
    Font(R.font.roboto, weight = FontWeight.Normal)
)
val Inter = FontFamily(
    Font(R.font.inter, weight = FontWeight.Normal)
)
val CinemaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
)



@Composable
fun FlixyConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CinemaTypography,
        content = content
    )
}

