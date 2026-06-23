package com.deveshsharma.lifeossixlyart.features.growth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryButton
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryTextField
import com.deveshsharma.lifeossixlyart.core.presentation.components.SectionHeader

@Composable
fun GrowthScreen() {
    var taskName by rememberSaveable { mutableStateOf("") }
    var eventName by rememberSaveable { mutableStateOf("") }
    var projectName by rememberSaveable { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionHeader(
                title = "Progress & Potential",
                subtitle = "Growth"
            )
            Text(
                text = "Shape your future by tending to the tasks and projects of today.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        item {
            SanctuaryCard {
                Text("Create New Task", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                SanctuaryTextField(value = taskName, onValueChange = { taskName = it}, label = "TASK NAME", placeholder = "What are you working on?")
                Spacer(modifier = Modifier.height(16.dp))
                SanctuaryButton(text = "ADD TASK", onClick = {}, icon = Icons.Default.Add)
            }
        }

        item {
            SanctuaryCard {
                Text("Add New Event", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                SanctuaryTextField(value = eventName, onValueChange = { eventName = it}, label = "EVENT NAME", placeholder = "What's the event?")
                Spacer(modifier = Modifier.height(16.dp))
                SanctuaryButton(text = "ADD EVENT", onClick = {})
            }
        }

        item {
            SanctuaryCard {
                Text("Add New Project", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                SanctuaryTextField(value = projectName, onValueChange = { projectName = it}, label = "PROJECT NAME", placeholder = "What's this project?")
                Spacer(modifier = Modifier.height(16.dp))
                SanctuaryButton(text = "ADD PROJECT", onClick = {})
            }
        }
    }
}
