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
const val slightlyDeemphasizedAlpha = 0.8f

private val DarkColorPalette = darkColors(
    primary = DarkColorPrimaryColor,            //white
    primaryVariant = DarkColorPrimaryDarkColor, // Should be non [White Card - min gray]
    onPrimary = DarkColorOnPrimaryTextColor,      ///


    secondary = DarkColorSecondaryColor,             //Purple
    secondaryVariant = DarkColorSecondaryDarkColor, //Yellow (complimentary)
    onSecondary = DarkColorOnSecondaryTextColor,       //White

    error = DarkColorErrorColor,               //Red (wrong answer)
    onError = DarkColorOnErrorLightColor,               //white (text)

    surface = DarkColorSurfaceColor,
    onSurface = DarkColorOnSurfaceColor,
    background = DarkColorBackgroundColor,
    onBackground = DarkColorOnBackgroundColor

)

private val LightColorPalette = lightColors(
    primary = ColorPrimaryColor,            //white
    primaryVariant = ColorPrimaryDarkColor, // Should be non [White Card - min gray]
    onPrimary = ColorOnPrimaryTextColor,      ///


    secondary = ColorSecondaryColor,             //Purple
    secondaryVariant = ColorSecondaryDarkColor, //Yellow (complimentary)
    onSecondary = ColorOnSecondaryTextColor,       //White

    error = ColorErrorColor,               //Red (wrong answer)
    onError = ColorOnErrorLightColor,               //white (text)

    surface = ColorSurfaceColor,
    onSurface = ColorOnSurfaceColor,
    background = ColorBackgroundColor,
    onBackground = ColorOnBackgroundColor

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