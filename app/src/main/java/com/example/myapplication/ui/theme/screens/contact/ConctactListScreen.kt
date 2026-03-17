package com.example.myapplication.ui.screens.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.entity.ContactEntity
import com.example.myapplication.data.viewmodel.ContactViewModel
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.navigation.AppScreens

/**
 * Pantalla que muestra la lista de todos los contactos.
 * Permite buscar, editar y eliminar contactos existentes,
 * y navegar al formulario para agregar uno nuevo.
 *
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que provee los datos de contactos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    navController: NavController,
    viewModel: ContactViewModel
) {
    /**
     * Recolecta el StateFlow del ViewModel como estado de Compose.
     * Cada vez que la lista cambie en la base de datos,
     * esta variable se actualiza y la UI se redibuja automáticamente.
     */
    val contacts by viewModel.allContacts.collectAsState()

    /**
     * Estado local para el texto de búsqueda.
     * remember mantiene el valor aunque la pantalla se redibuje.
     */
    var searchQuery by remember { mutableStateOf("") }

    /**
     * Filtra la lista de contactos según el texto de búsqueda.
     * Busca coincidencias en nombre y teléfono ignorando mayúsculas.
     */
    val filteredContacts = contacts.filter { contact ->
        contact.name.contains(searchQuery, ignoreCase = true) ||
                contact.phone.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contactos",
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
         * FloatingActionButton: botón flotante para agregar un nuevo contacto.
         * Navega al formulario en modo agregar (contactId = -1).
         */
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreens.ContactForm.createRoute(-1))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar contacto",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // ─── Barra de búsqueda ─────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre o teléfono...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(16.dp))

            // ─── Lista o mensaje vacío ─────────────────────
            if (filteredContacts.isEmpty()) {
                /**
                 * Si no hay contactos o no hay resultados de búsqueda,
                 * muestra un mensaje centrado en la pantalla.
                 */
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isEmpty())
                            "No hay contactos aún.\nToca + para agregar uno."
                        else
                            "No se encontraron resultados para \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                /**
                 * LazyColumn: lista eficiente que solo renderiza
                 * los elementos visibles en pantalla.
                 * Ideal para listas largas.
                 */
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredContacts,
                        key = { contact -> contact.id }
                    ) { contact ->
                        ContactItem(
                            contact = contact,
                            onEditClick = {
                                // Navega al formulario en modo editar
                                navController.navigate(
                                    AppScreens.ContactForm.createRoute(contact.id)
                                )
                            },
                            onDeleteClick = {
                                viewModel.delete(contact)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente que representa una tarjeta de contacto en la lista.
 * Muestra nombre y teléfono, con botones para editar y eliminar.
 *
 * @param contact El contacto a mostrar.
 * @param onEditClick Acción al tocar el botón editar.
 * @param onDeleteClick Acción al tocar el botón eliminar.
 */
@Composable
fun ContactItem(
    contact: ContactEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ─── Datos del contacto ────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = contact.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ─── Botones de acción ─────────────────────────
            Row {
                // Botón editar
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar contacto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Botón eliminar
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar contacto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
