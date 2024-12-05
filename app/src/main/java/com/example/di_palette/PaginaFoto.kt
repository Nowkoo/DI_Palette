package com.example.di_palette

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette

@SuppressLint("RememberReturnType")
@Composable
fun PaginaFoto(nombre: String?, foto: Int?, modifier: Modifier) {
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    if (foto != null) {
        val context = LocalContext.current
        val bitmap = remember {
            BitmapFactory.decodeResource(context.resources, foto)
        }
        val palette = remember {
            Palette.from(bitmap).generate()
        }

        val darkVibrant = palette.darkVibrantSwatch?.let { Color(it.rgb) } ?: Color.Transparent
        changeStatusBarColor(darkVibrant)

        LaunchedEffect(Unit) {
            visible = true
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            createTopAppBar(palette.vibrantSwatch)

            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    // Expand from the top.
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                ),
                exit = slideOutHorizontally()
            ) {
                Image(
                    painter = painterResource(foto),
                    contentDescription = nombre,
                    Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    // Expand from the top.
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                ),
                exit = slideOutHorizontally()
            ) {
                Column {
                    ColoredRow(palette.lightVibrantSwatch, "Light Vibrant")
                    ColoredRow(palette.darkVibrantSwatch, "Dark Vibrant")
                    ColoredRow(palette.lightMutedSwatch, "Light Muted")
                    ColoredRow(palette.mutedSwatch, "Muted")
                    ColoredRow(palette.darkMutedSwatch, "Dark Muted")
                    Box(
                        Modifier.background(Color.White).height(1000.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ColoredRow(swatch: Palette.Swatch?, text: String) {
    Text(
        text = if (swatch == null) "$text no encontrado" else text,
        textAlign = TextAlign.Center,
        color = swatch?.let {Color(it.titleTextColor)} ?: Color.Black,
        modifier = Modifier
            .background(
                color = swatch?.let { Color(it.rgb) } ?: Color.Transparent
            )
            .fillMaxWidth()
            .height(50.dp)
    )
}