package com.example.di_palette

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette

@SuppressLint("RememberReturnType")
@Composable
fun PaginaFoto(nombre: String?, foto: Int?, modifier: Modifier) {
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            createTopAppBar(palette.vibrantSwatch)
            Image(
                painter = painterResource(foto),
                contentDescription = nombre,
                Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            ColoredRow(palette.lightVibrantSwatch!!, "Light Vibrant")
            ColoredRow(palette.darkVibrantSwatch!!, "Dark Vibrant")
            ColoredRow(palette.lightMutedSwatch!!, "Light Muted")
            ColoredRow(palette.mutedSwatch!!, "Muted")
            ColoredRow(palette.darkMutedSwatch!!, "Dark Muted")
        }
    }
}

@Composable
fun ColoredRow(swatch: Palette.Swatch?, text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = swatch?.let {Color(it.titleTextColor)} ?: Color.Transparent,
        modifier = Modifier
            .background(
                color = swatch?.let { Color(it.rgb) } ?: Color.Transparent
            )
            .fillMaxWidth()
            .height(50.dp)
    )
}