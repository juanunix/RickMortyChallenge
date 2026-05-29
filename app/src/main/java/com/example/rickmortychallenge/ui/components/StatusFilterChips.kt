package com.example.rickmortychallenge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusFilterChips(
    selectedStatus: String?,
    onStatusSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = listOf("Alive", "Dead", "Unknown")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            label = { Text("All") }
        )

        statuses.forEach { status ->
            FilterChip(
                selected = selectedStatus?.lowercase() == status.lowercase(),
                onClick = { onStatusSelected(status) },
                label = { Text(status) }
            )
        }
    }
}
