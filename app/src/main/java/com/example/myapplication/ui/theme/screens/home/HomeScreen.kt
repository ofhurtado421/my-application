package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.navigation.AppScreens

/**
 * Pantalla de inicio de la aplicación.
 * Muestra 3 tarjetas para acceder a cada módulo:
 * Contactos, Productos y Proveedores.
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        /**
         * TopAppBar: barra superior con el título de la app.
         */
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Negocio",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        /**
         * BottomNavigationBar: barra inferior con los 3 módulos.
         * Permite navegar entre pantallas desde cualquier lugar.
         */
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Título de bienvenida
            Text(
                text = "¿Qué deseas gestionar?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ─── Tarjeta Contactos ─────────────────────────
            ModuleCard(
                title = "Clientes",
                description = "Gestiona tus clientes",
                icon = Icons.Filled.Person,
                onClick = {
                    navController.navigate(AppScreens.ContactList.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Tarjeta Productos ─────────────────────────
            ModuleCard(
                title = "Productos",
                description = "Gestiona tu catálogo",
                icon = Icons.Filled.ShoppingCart,
                onClick = {
                    navController.navigate(AppScreens.ProductList.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Tarjeta Proveedores ───────────────────────
            ModuleCard(
                title = "Proveedores",
                description = "Gestiona tus proveedores",
                icon = Icons.Filled.Store,
                onClick = {
                    navController.navigate(AppScreens.ProviderList.route)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ModuleCard(
                title = "Acerca de",
                description = "Créditos y documentación",
                icon = Icons.Filled.Info,
                onClick = {
                    navController.navigate(AppScreens.About.route)
                }
            )


        }

    }
}

/**
 * Componente reutilizable que representa una tarjeta de módulo.
 * Al tocarla navega a la pantalla correspondiente.
 *
 * @param title Título del módulo.
 * @param description Descripción corta del módulo.
 * @param icon Ícono representativo del módulo.
 * @param onClick Acción al tocar la tarjeta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Ícono del módulo
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.size(16.dp))

            // Título y descripción
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
