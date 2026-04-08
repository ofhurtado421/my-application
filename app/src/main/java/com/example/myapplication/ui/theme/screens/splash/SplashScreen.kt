package com.example.myapplication.ui.theme.screens.splash


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AppScreens
import kotlinx.coroutines.delay

/**
 * Pantalla de bienvenida (Splash Screen).
 * Se muestra al abrir la app durante 2 segundos con
 * una animación de aparición gradual (fade in).
 * Luego navega automáticamente al Home..
 *
 * @param navController Controlador de navegación.
 */
@Composable
fun SplashScreen(navController: NavController) {

    /**
     * Controla si la animación debe iniciar.
     * Empieza en false y cambia a true al cargar la pantalla.
     */
    var startAnimation by remember { mutableStateOf(false) }

    /**
     * animateFloatAsState anima el valor de alpha (transparencia)
     * de 0f (invisible) a 1f (visible) en 1200 milisegundos.
     * Esto crea el efecto de aparición gradual (fade in).
     */
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "splash_alpha"
    )

    /**
     * LaunchedEffect se ejecuta una sola vez al entrar a la pantalla.
     * 1. Activa la animación de fade in.
     * 2. Espera 2500 milisegundos (2.5 segundos).
     * 3. Navega al Home eliminando el Splash del backstack
     *    para que al presionar atrás no regrese al Splash.
     */
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        navController.navigate(AppScreens.Home.route) {
            popUpTo(AppScreens.Splash.route) {
                inclusive = true // elimina el Splash del backstack
            }
        }
    }

    /**
     * Contenido visual de la pantalla.
     * Fondo con color primario del tema y texto centrado.
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .alpha(alphaAnim), // aplica la animación de transparencia
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ─── Título principal ──────────────────────────────
        Text(
            text = "MI NEGOCIO",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ─── Subtítulo ─────────────────────────────────────
        Text(
            text = "Gestión inteligente",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
    }
}