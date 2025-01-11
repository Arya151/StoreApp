package com.store.decathlon.presentation.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.store.decathlon.domain.model.ProductModel
import com.store.decathlon.util.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(viewModel: HomeViewModel) {
    val searchQuery = remember { mutableStateOf("") }
    val selectedSortOption = remember { mutableStateOf(SortOption.BY_PRICE) }
    val items by viewModel.products.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val isLoadingMore by viewModel.isLoadingMore.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Row with Search Bar and Spinner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Bar (TextField)
            TextField(
                value = searchQuery.value,
                onValueChange = {
                    searchQuery.value = it
                    viewModel.searchProducts(it) // Trigger search in ViewModel
                },
                label = { Text("Search by name or brand") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            // Spinner (Dropdown Menu)
            var expanded by remember { mutableStateOf(false) }
            val sortOptions = listOf(SortOption.BY_PRICE, SortOption.BY_NAME)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = when (selectedSortOption.value) {
                        SortOption.BY_PRICE -> "Price"
                        SortOption.BY_NAME -> "Name"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sort By") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .clickable { expanded = true }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(if (option == SortOption.BY_PRICE) "Price" else "Name") },
                            onClick = {
                                selectedSortOption.value = option
                                expanded = false
                                viewModel.sortProducts(option) // Trigger sorting in ViewModel
                            }
                        )
                    }
                }
            }
        }

        // Loading indicator
        Log.d("homepage","isLoading - $isLoading, items size - ${items.size}")

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // List of items with Pagination
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    Log.d("homepage","item - $item")
                    ProductCard(
                        product = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

                // Display loading more indicator when reaching the end of the list
                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(text = product.name, fontWeight = FontWeight.Bold)
                Text(text = "Price: ${product.price}")
                Text(text = "Brand: ${product.brand}")
            }
        }
    }
}

