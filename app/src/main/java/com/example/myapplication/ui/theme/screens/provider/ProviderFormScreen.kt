package com.example.myapplication.ui.screens.provider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.myapplication.data.entity.ProviderEntity
import com.example.myapplication.data.viewmodel.ProviderViewModel

/**
 * Pantalla de formulario para agregar o editar un proveedor.
 * Si providerId == -1 → modo agregar.
 * Si providerId > 0  → modo editar.
 *
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona los datos.
 * @param providerId ID del proveedor a editar, o -1 para agregar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderFormScreen(
    navController: NavController,
    viewModel: ProviderViewModel,
    providerId: Int
) {
    var businessName by remember { mutableStateOf("") }
    var nit by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val isEditMode = providerId != -1

    if (isEditMode) {
        val provider by viewModel.getProviderById(providerId).collectAsState(initial = null)
        LaunchedEffect(provider) {
            provider?.let {
                businessName = it.businessName
                nit = it.nit
                phone = it.phone
                address = it.address
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Editar Proveedor" else "Nuevo Proveedor",
                        fontWeight = FontWeight.Bold
                    )
                },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ─── Nombre comercial ──────────────────────────
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text("Nombre comercial") },
                placeholder = { Text("Ej: Distribuidora El Mayoreo S.A.S") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && businessName.isBlank(),
                supportingText = {
                    if (showErrors && businessName.isBlank())
                        Text("El nombre comercial es obligatorio",
                            color = MaterialTheme.colorScheme.error)
                }
            )

            // ─── NIT ───────────────────────────────────────
            OutlinedTextField(
                value = nit,
                onValueChange = { nit = it },
                label = { Text("NIT") },
                placeholder = { Text("Ej: 900123456-1") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && nit.isBlank(),
                supportingText = {
                    if (showErrors && nit.isBlank())
                        Text("El NIT es obligatorio",
                            color = MaterialTheme.colorScheme.error)
                }
            )

            // ─── Teléfono ──────────────────────────────────
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                placeholder = { Text("Ej: 3001234567") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                isError = showErrors && phone.isBlank(),
                supportingText = {
                    if (showErrors && phone.isBlank())
                        Text("El teléfono es obligatorio",
                            color = MaterialTheme.colorScheme.error)
                }
            )

            // ─── Dirección ─────────────────────────────────
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección") },
                placeholder = { Text("Ej: Calle 15 #32-10, Cali") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ─── Botón Guardar ─────────────────────────────
            Button(
                onClick = {
                    if (businessName.isBlank() || nit.isBlank() || phone.isBlank()) {
                        showErrors = true
                    } else {
                        if (isEditMode) {
                            viewModel.update(
                                ProviderEntity(
                                    id = providerId,
                                    businessName = businessName.trim(),
                                    nit = nit.trim(),
                                    phone = phone.trim(),
                                    address = address.trim()
                                )
                            )
                        } else {
                            viewModel.insert(
                                ProviderEntity(
                                    businessName = businessName.trim(),
                                    nit = nit.trim(),
                                    phone = phone.trim(),
                                    address = address.trim()
                                )
                            )
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (isEditMode) "Actualizar Proveedor" else "Guardar Proveedor",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
