package com.example.myapplication.ui.screens.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.entity.PurchaseDetailEntity
import com.example.myapplication.data.entity.PurchaseEntity
import com.example.myapplication.data.viewmodel.ProductViewModel
import com.example.myapplication.data.viewmodel.PurchaseViewModel
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla de formulario para crear una nueva compra.
 * Permite ingresar el proveedor, agregar productos
 * por nombre o código de barras y calcular el total.
 *
 * @param navController Controlador de navegación.
 * @param purchaseViewModel ViewModel de compras.
 * @param productViewModel ViewModel de productos para buscarlos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseFormScreen(
    navController: NavController,
    purchaseViewModel: PurchaseViewModel,
    productViewModel: ProductViewModel
) {
    var providerName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var searchProduct by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    var productNotFound by remember { mutableStateOf(false) }

    val purchaseDetails = remember { mutableStateListOf<PurchaseDetailEntity>() }
    val allProducts by productViewModel.allProducts.collectAsState()
    val total = purchaseDetails.sumOf { it.subtotal }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Nueva Compra", fontWeight = FontWeight.Bold)
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ─── Campo Proveedor ───────────────────────────
            item {
                OutlinedTextField(
                    value = providerName,
                    onValueChange = { providerName = it },
                    label = { Text("Nombre del proveedor") },
                    placeholder = { Text("Ej: Distribuidora XYZ") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showErrors && providerName.isBlank(),
                    supportingText = {
                        if (showErrors && providerName.isBlank())
                            Text(
                                "El proveedor es obligatorio",
                                color = MaterialTheme.colorScheme.error
                            )
                    }
                )
            }

            // ─── Buscador de productos ─────────────────────
            item {
                Text(
                    text = "Agregar productos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchProduct,
                        onValueChange = {
                            searchProduct = it
                            productNotFound = false
                        },
                        label = { Text("Nombre o código de barras") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            val query = searchProduct.trim()
                            val product = allProducts.find {
                                it.barcode == query ||
                                        it.name.contains(query, ignoreCase = true)
                            }

                            if (product != null) {
                                val existing = purchaseDetails.indexOfFirst {
                                    it.productId == product.id
                                }
                                if (existing >= 0) {
                                    val current = purchaseDetails[existing]
                                    val newQty = current.quantity + 1
                                    purchaseDetails[existing] = current.copy(
                                        quantity = newQty,
                                        subtotal = newQty * current.unitPrice
                                    )
                                } else {
                                    purchaseDetails.add(
                                        PurchaseDetailEntity(
                                            purchaseId = 0,
                                            productId = product.id,
                                            productName = product.name,
                                            quantity = 1,
                                            /**
                                             * Usa purchasePrice (precio de compra)
                                             * no salePrice (precio de venta).
                                             */
                                            unitPrice = product.purchasePrice,
                                            subtotal = product.purchasePrice
                                        )
                                    )
                                }
                                searchProduct = ""
                                productNotFound = false
                            } else {
                                productNotFound = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar producto",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (productNotFound) {
                    Text(
                        text = "⚠️ Producto no encontrado",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ─── Lista de productos agregados ──────────────
            if (purchaseDetails.isNotEmpty()) {
                item {
                    Text(
                        text = "Productos en la compra",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(purchaseDetails) { detail ->
                    PurchaseDetailItem(
                        detail = detail,
                        onQuantityChange = { newQty ->
                            val index = purchaseDetails.indexOf(detail)
                            if (index >= 0 && newQty > 0) {
                                purchaseDetails[index] = detail.copy(
                                    quantity = newQty,
                                    subtotal = newQty * detail.unitPrice
                                )
                            }
                        },
                        onRemove = { purchaseDetails.remove(detail) }
                    )
                }

                // ─── Total ─────────────────────────────────
                item {
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TOTAL:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currencyFormat.format(total),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // ─── Campo Notas ───────────────────────────────
            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas (opcional)") },
                    placeholder = { Text("Ej: Factura #123") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }

            // ─── Botón Guardar Compra ──────────────────────
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (providerName.isBlank() || purchaseDetails.isEmpty()) {
                            showErrors = true
                        } else {
                            /**
                             * ✅ Corrección: se usa providerName directamente
                             * en lugar de providerId = 0 que causaba el error
                             * de ForeignKey constraint failed.
                             */
                            val purchase = PurchaseEntity(
                                providerName = providerName.trim(),
                                total = total,
                                notes = notes.trim()
                            )
                            purchaseViewModel.insertPurchaseWithDetails(
                                purchase,
                                purchaseDetails.toList()
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Guardar Compra",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (showErrors && purchaseDetails.isEmpty()) {
                    Text(
                        text = "⚠️ Agrega al menos un producto",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta que muestra un producto agregado a la compra.
 * Permite cambiar la cantidad o eliminar el producto.
 *
 * @param detail El detalle del producto.
 * @param onQuantityChange Acción al cambiar la cantidad.
 * @param onRemove Acción al eliminar el producto.
 */
@Composable
fun PurchaseDetailItem(
    detail: PurchaseDetailEntity,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = detail.productName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Precio: ${currencyFormat.format(detail.unitPrice)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { if (detail.quantity > 1) onQuantityChange(detail.quantity - 1) },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = " ${detail.quantity} ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedButton(
                        onClick = { onQuantityChange(detail.quantity + 1) },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = currencyFormat.format(detail.subtotal),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}