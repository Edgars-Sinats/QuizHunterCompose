@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.quizhuntercompose.ui.theme

import android.app.Activity
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.key.Key.Companion.Window
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.compose.material.lightColors


const val stronglyDeemphasizedAlpha = 0.6f
const val slightlyDeemphasizedAlpha = 0.87f

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = PurpleGrey80,
    onPrimary = Color.Green
)

private val LightColorPalette = lightColors(
    primary = ColorPrimaryColor,
    primaryVariant = ColorPrimaryLightColor,
    onPrimary = ColorPrimaryTextColor,

    secondary = ColorSecondaryColor,
    secondaryVariant = ColorSecondaryLightColor, //Green (correct answer)
    onSecondary = ColorSecondaryTextColor,

    error = ColorErrorLightColor,               //Red (wrong answer)
    onError = ColorOnErrorLightColor,

    surface = ColorSecondaryDarkColor,
    onSurface = ColorOnSurfaceColor,
    background = ColorPrimaryDarkColor,

//    onBackground = ColorOnSurfaceColor,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val HunterColors: Colors
    @Composable get() = MaterialTheme.colors

val HunterShapes: Shapes
    @Composable get() = MaterialTheme.shapes

val HunterTypography: Typography
    @Composable get() = MaterialTheme.typography

@Composable
fun QuizHunterComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController((view.context as Activity).window, view)?.isAppearanceLightStatusBars = darkTheme
//            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}