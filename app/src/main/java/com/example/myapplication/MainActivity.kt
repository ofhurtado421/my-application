package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.AppNavGraph
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Actividad principal de la aplicación.
 * Es el punto de entrada cuando el usuario abre la app.
 * Solo se encarga de inicializar Compose y el sistema de navegación,
 * toda la lógica está en los ViewModels y Repositorios.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * setContent reemplaza el sistema tradicional de XML.
         * Todo lo que se ponga aquí dentro se renderiza en pantalla
         * usando Jetpack Compose.
         */
        setContent {

            /**
             * MyApplicationTheme aplica los colores y tipografía
             * definidos en ui/theme/ a toda la aplicación.
             */
            MyApplicationTheme {

                /**
                 * Surface es el contenedor base de Material Design.
                 * fillMaxSize hace que ocupe toda la pantalla.
                 * color define el color de fondo usando el tema actual.
                 */
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /**
                     * rememberNavController crea y recuerda el controlador
                     * de navegación durante toda la vida de la actividad.
                     * Es el objeto que permite moverse entre pantallas.
                     */
                    val navController = rememberNavController()

                    /**
                     * AppNavGraph recibe el navController y se encarga
                     * de mostrar la pantalla correcta según la ruta actual.
                     * Aquí es donde arranca toda la navegación de la app.
                     */
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}
