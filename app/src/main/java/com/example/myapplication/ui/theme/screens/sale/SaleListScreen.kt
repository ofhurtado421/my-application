package com.example.myapplication.ui.screens.sale

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.example.myapplication.data.entity.SaleEntity
import com.example.myapplication.data.viewmodel.SaleViewModel
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.navigation.AppScreens
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pantalla que muestra la lista de todas las ventas.
 * Permite buscar y eliminar ventas existentes,
 * y navegar al formulario para agregar una nueva.
 *
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que provee los datos de ventas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleListScreen(
    navController: NavController,
    viewModel: SaleViewModel
) {
    val sales by viewModel.allSales.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    /**
     * Filtra ventas por nombre del cliente o notas.
     */
    val filteredSales = sales.filter { sale ->
        sale.notes.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Ventas", fontWeight = FontWeight.Bold)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreens.SaleForm.createRoute(-1))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva venta",
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por notas...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(16.dp))

            if (filteredSales.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isEmpty())
                            "No hay ventas aún.\nToca + para agregar una."
                        else
                            "No se encontraron resultados para \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(
                        items = filteredSales,
                        key = { sale -> sale.id }
                    ) { sale ->
                        SaleItem(
                            sale = sale,
                            onDeleteClick = { viewModel.deleteSale(sale) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta que muestra el resumen de una venta.
 * Muestra fecha, cliente y total.
 *
 * @param sale La venta a mostrar.
 * @param onDeleteClick Acción al tocar eliminar.
 */
@Composable
fun SaleItem(
    sale: SaleEntity,
    onDeleteClick: () -> Unit
) {
    /**
     * Formatea la fecha del timestamp a formato legible.
     * Ejemplo: "09/04/2026 10:30"
     */
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(sale.date))

    /**
     * Formatea el total como moneda colombiana.
     * Ejemplo: "$ 17.000"
     */
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    val formattedTotal = currencyFormat.format(sale.total)

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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Venta #${sale.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fecha: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Total: $formattedTotal",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (sale.notes.isNotEmpty()) {
                    Text(
                        text = "Nota: ${sale.notes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar venta",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}