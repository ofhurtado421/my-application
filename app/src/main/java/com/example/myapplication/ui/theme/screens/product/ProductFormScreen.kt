package com.example.myapplication.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.entity.ProductEntity
import com.example.myapplication.data.viewmodel.ProductViewModel

/**
 * Pantalla de formulario para agregar o editar un producto.
 * Si productId == -1 → modo agregar.
 * Si productId > 0  → modo editar.
 *
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona los datos.
 * @param productId ID del producto a editar, o -1 para agregar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    productId: Int
) {
    // ─── Estados de cada campo ─────────────────────────────
    var barcode by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var unitType by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val isEditMode = productId != -1

    if (isEditMode) {
        val product by viewModel.getProductById(productId).collectAsState(initial = null)
        LaunchedEffect(product) {
            product?.let {
                barcode = it.barcode
                name = it.name
                description = it.description
                category = it.category
                unitType = it.unitType
                manufacturer = it.manufacturer
                brand = it.brand
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Editar Producto" else "Nuevo Producto",
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

        /**
         * verticalScroll permite hacer scroll en el formulario
         * ya que tiene muchos campos y no caben todos en pantalla.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ─── Código de barras ──────────────────────────
            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Código de barras") },
                placeholder = { Text("Ej: 7501234567890") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && barcode.isBlank(),
                supportingText = {
                    if (showErrors && barcode.isBlank())
                        Text("El código de barras es obligatorio",
                            color = MaterialTheme.colorScheme.error)
                }
            )

            // ─── Nombre ────────────────────────────────────
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del producto") },
                placeholder = { Text("Ej: Arroz Diana 500g") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && name.isBlank(),
                supportingText = {
                    if (showErrors && name.isBlank())
                        Text("El nombre es obligatorio",
                            color = MaterialTheme.colorScheme.error)
                }
            )

            // ─── Descripción ───────────────────────────────
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                placeholder = { Text("Ej: Arroz blanco de grano largo") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // ─── Categoría ─────────────────────────────────
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                placeholder = { Text("Ej: Abarrotes, Limpieza, Bebidas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && category.isBlank(),
                supportingText = {
                    if (showErrors && category.isBlank())
                        Text("La categoría es obligatoria",
                            color = MaterialTheme.colorScheme.error)
                }
            )

            // ─── Tipo de unidad ────────────────────────────
            OutlinedTextField(
                value = unitType,
                onValueChange = { unitType = it },
                label = { Text("Tipo de unidad") },
                placeholder = { Text("Ej: Kilogramos, Litros, Piezas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // ─── Fabricante ────────────────────────────────
            OutlinedTextField(
                value = manufacturer,
                onValueChange = { manufacturer = it },
                label = { Text("Fabricante") },
                placeholder = { Text("Ej: Nestlé, P&G") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // ─── Marca ─────────────────────────────────────
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Marca") },
                placeholder = { Text("Ej: Ariel, Maggi") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ─── Botón Guardar ─────────────────────────────
            Button(
                onClick = {
                    if (barcode.isBlank() || name.isBlank() || category.isBlank()) {
                        showErrors = true
                    } else {
                        if (isEditMode) {
                            viewModel.update(
                                ProductEntity(
                                    id = productId,
                                    barcode = barcode.trim(),
                                    name = name.trim(),
                                    description = description.trim(),
                                    category = category.trim(),
                                    unitType = unitType.trim(),
                                    manufacturer = manufacturer.trim(),
                                    brand = brand.trim()
                                )
                            )
                        } else {
                            viewModel.insert(
                                ProductEntity(
                                    barcode = barcode.trim(),
                                    name = name.trim(),
                                    description = description.trim(),
                                    category = category.trim(),
                                    unitType = unitType.trim(),
                                    manufacturer = manufacturer.trim(),
                                    brand = brand.trim()
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
                    text = if (isEditMode) "Actualizar Producto" else "Guardar Producto",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}