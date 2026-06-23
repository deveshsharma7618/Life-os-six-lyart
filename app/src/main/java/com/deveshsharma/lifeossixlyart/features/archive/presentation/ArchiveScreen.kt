package com.deveshsharma.lifeossixlyart.features.archive.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.deveshsharma.lifeossixlyart.core.presentation.components.SectionHeader

@Composable
fun ArchiveScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionHeader(
                title = "Chronicles & Echoes",
                subtitle = "Archive"
            )
            Text(
                text = "Explore past journal entries and review metrics history.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        item {
            SanctuaryCard {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search entries, tasks or remarks") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0 entries", style = MaterialTheme.typography.bodySmall)
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("EXPORT CSV", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        item {
            SanctuaryCard {
                Text("OVERVIEW", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                ArchiveStatRow("Days Logged", "0")
                ArchiveStatRow("Average Rating", "—")
                ArchiveStatRow("Tasks Done", "0")
                ArchiveStatRow("Avg Productive Time", "—")
            }
        }
    }
}

@Composable
fun ArchiveStatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
    }
}
