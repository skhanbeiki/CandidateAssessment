package net.ragham.candidateassessment.ui.invoice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.ragham.candidateassessment.viewmodel.InvoiceViewModel

// صفحه اصلی ویرایشگر فاکتور.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceEditorScreen(
    viewModel: InvoiceViewModel,
    onAddItem: () -> Unit,
    onEditItem: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("فاکتور فروش") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                InvoiceSummary(
                    totalBeforeDiscount = uiState.totalBeforeDiscount,
                    totalDiscount = uiState.totalDiscount,
                    finalTotal = uiState.finalTotal
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(uiState.items, key = { it.id }) { item ->
                InvoiceItemRow(
                    item = item,
                    onDelete = { viewModel.removeItem(item) },
                    onEdit = { onEditItem(item.id) }
                )
                HorizontalDivider()
            }
            
            if (uiState.items.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "آیتمی وجود ندارد. دکمه + را بزنید.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
