package com.example.myapplication.ui.screens.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.entity.ContactEntity
import com.example.myapplication.data.viewmodel.ContactViewModel

/**
 * Pantalla de formulario para agregar o editar un contacto.
 * Si contactId == -1 → modo agregar (contacto nuevo).
 * Si contactId > 0  → modo editar (contacto existente).
 *
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que provee y gestiona los datos.
 * @param contactId ID del contacto a editar, o -1 para agregar uno nuevo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    navController: NavController,
    viewModel: ContactViewModel,
    contactId: Int
) {
    /**
     * Estados locales para cada campo del formulario.
     * remember mantiene los valores aunque la pantalla se redibuje.
     * mutableStateOf hace que al cambiar el valor se redibuje la UI.
     */
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    /**
     * Estado para controlar si el usuario intentó guardar
     * sin llenar los campos obligatorios.
     * Se usa para mostrar mensajes de error en los campos.
     */
    var showErrors by remember { mutableStateOf(false) }

    /**
     * Determina si estamos en modo editar o agregar
     * según el valor de contactId.
     */
    val isEditMode = contactId != -1

    /**
     * Si estamos en modo editar, buscamos el contacto por ID
     * y cargamos sus datos en los campos del formulario.
     */
    if (isEditMode) {
        val contact by viewModel.getContactById(contactId).collectAsState(initial = null)

        /**
         * LaunchedEffect se ejecuta una sola vez cuando
         * el contacto es cargado desde la base de datos.
         * Llena los campos con los datos existentes.
         */
        LaunchedEffect(contact) {
            contact?.let {
                name = it.name
                phone = it.phone
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        /**
                         * El título cambia según el modo:
                         * "Nuevo Contacto" o "Editar Contacto"
                         */
                        text = if (isEditMode) "Editar Contacto" else "Nuevo Contacto",
                        fontWeight = FontWeight.Bold
                    )
                },
                /**
                 * Botón de regreso en la barra superior.
                 * Vuelve a la pantalla anterior sin guardar.
                 */
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // ─── Campo Nombre ──────────────────────────────
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                placeholder = { Text("Ej: Juan Pérez") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                /**
                 * isError muestra el campo en rojo si showErrors es true
                 * y el campo está vacío.
                 */
                isError = showErrors && name.isBlank(),
                supportingText = {
                    if (showErrors && name.isBlank()) {
                        Text(
                            text = "El nombre es obligatorio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ─── Campo Teléfono ────────────────────────────
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                placeholder = { Text("Ej: 3001234567") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                /**
                 * KeyboardType.Phone muestra el teclado numérico
                 * al tocar este campo.
                 */
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                isError = showErrors && phone.isBlank(),
                supportingText = {
                    if (showErrors && phone.isBlank()) {
                        Text(
                            text = "El teléfono es obligatorio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ─── Botón Guardar ─────────────────────────────
            Button(
                onClick = {
                    /**
                     * Valida que los campos no estén vacíos.
                     * Si hay errores los muestra, si no guarda.
                     */
                    if (name.isBlank() || phone.isBlank()) {
                        showErrors = true
                    } else {
                        if (isEditMode) {
                            /**
                             * Modo editar: actualiza el contacto existente
                             * conservando el mismo ID.
                             */
                            viewModel.update(
                                ContactEntity(
                                    id = contactId,
                                    name = name.trim(),
                                    phone = phone.trim()
                                )
                            )
                        } else {
                            /**
                             * Modo agregar: inserta un nuevo contacto.
                             * El ID se genera automáticamente (id = 0).
                             */
                            viewModel.insert(
                                ContactEntity(
                                    name = name.trim(),
                                    phone = phone.trim()
                                )
                            )
                        }
                        // Regresa a la lista después de guardar
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (isEditMode) "Actualizar Contacto" else "Guardar Contacto",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
