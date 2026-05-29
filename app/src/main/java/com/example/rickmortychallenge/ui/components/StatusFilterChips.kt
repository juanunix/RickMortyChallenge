package com.example.rickmortychallenge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rickmortychallenge.theme.Primary
import com.example.rickmortychallenge.theme.Secondary

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
        // "All" chip
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            shape = CircleShape, // Pill shape rounding
            label = { 
                Text(
                    text = "ALL", 
                    style = MaterialTheme.typography.labelSmall
                ) 
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Primary,
                selectedLabelColor = Color(0xFF101416),
                containerColor = Color(0xFF202329),
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = FilterChipDefaults.filterChipBorder(
                borderColor = Color(0x1AFFFFFF),
                selectedBorderColor = Primary,
                enabled = true,
                selected = selectedStatus == null
            )
        )

        statuses.forEach { status ->
            val isSelected = selectedStatus?.lowercase() == status.lowercase()
            FilterChip(
                selected = isSelected,
                onClick = { onStatusSelected(status) },
                shape = CircleShape, // Pill shape rounding
                label = { 
                    Text(
                        text = status.uppercase(), 
                        style = MaterialTheme.typography.labelSmall
                    ) 
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Secondary,
                    selectedLabelColor = Color(0xFF101416),
                    containerColor = Color(0xFF202329),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color(0x1AFFFFFF),
                    selectedBorderColor = Secondary,
                    enabled = true,
                    selected = isSelected
                )
            )
        }
    }
}
