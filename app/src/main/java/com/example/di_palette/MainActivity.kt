package com.example.di_palette

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.palette.graphics.Palette
import com.example.di_palette.ui.theme.DI_PaletteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DI_PaletteTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "PantallaInicial",
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        composable("PantallaInicial") {
                            PantallaInicial(Modifier.padding(innerPadding), navController)
                            changeStatusBarColor(Color.White)
                        }
                        composable(
                            "PaginaFoto/{place}/{foto}",
                            arguments = listOf(
                                navArgument("place") { type = NavType.StringType },
                                navArgument("foto") { type = NavType.IntType }
                            ),
//                            enterTransition = {
//                                slideInHorizontally(
//                                    animationSpec = tween(
//                                        300, easing = LinearEasing
//                                    ))
////                                ) + slideIntoContainer(
////                                    animationSpec = tween(300, easing = EaseIn),
////                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
////                                )
//                            },
                            exitTransition = {
                                shrinkHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                )
                            }
                        ) { backStackEntry ->
                            PaginaFoto(
                                backStackEntry.arguments?.getString("place"),
                                backStackEntry.arguments?.getInt("foto"),
                                Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaInicial(modifier: Modifier = Modifier, navController: NavController) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxWidth().padding(2.dp),
        content = {
            items(getPlaces().size) { index ->
                ItemPlace(getPlaces().get(index), navController)
            }
        }
    )
}

fun getPlaces(): List<Place> {
    return listOf(
        Place("Playa Algarve", R.drawable.image1),
        Place("Maldivas", R.drawable.image2),
        Place("Machu Pichu", R.drawable.image3),
        Place("Gran Muralla China", R.drawable.image4),
        Place("Alhambra", R.drawable.image5),
        Place("Atenas", R.drawable.image6),
        Place("Pirámide Kukulkan", R.drawable.image7),
        Place("Punta Cana", R.drawable.image8)
    )
}

@Composable
fun ItemPlace(place: Place, navController: NavController) {
    Box(
        Modifier
            .clickable { navController.navigate("PaginaFoto/${place.nombre}/${place.foto}") }
            .padding(2.dp)
    ) {
        Image(
            painter = painterResource(place.foto),
            contentDescription = place.nombre,
            Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Text(
                text = place.nombre,
                color = Color.White,
                fontSize = 22.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

data class Place (
    var nombre: String,
    @DrawableRes var foto: Int
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun createTopAppBar(swatch: Palette.Swatch?) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = swatch?.let { Color(it.rgb) } ?: Color.Transparent,
            titleContentColor = swatch?.let {Color(it.titleTextColor)} ?: Color.Transparent,
        ),
        title = {
            Text(
                "Palette",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        }
    )

}

@Composable
fun changeStatusBarColor(color: Color) {
    val view = LocalView.current
    val window =
        (view.context as Activity).window
    window.statusBarColor = color.toArgb()
}