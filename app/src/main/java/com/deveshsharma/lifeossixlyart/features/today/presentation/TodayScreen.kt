package com.deveshsharma.lifeossixlyart.features.today.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.deveshsharma.lifeossixlyart.core.presentation.components.SectionHeader
import com.deveshsharma.lifeossixlyart.features.today.presentation.components.MetricCard
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getDate() : String{
    val date = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEE, MMMM d")
    return date.format(formatter)
}

fun getGreeting(): String {
    val currTime = LocalDateTime.now()
    val hrs = currTime.hour
    return when (hrs) {
        in 0..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..20 -> "Good evening"
        else -> "Good night"
    }
}

@Composable
fun TodayScreen() {
    val todayViewModel: TodayViewModel = viewModel()
    val user_id = Firebase.auth.currentUser?.uid
    val currMood = todayViewModel.moodState.collectAsState()
    var dailyRemarks by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val thoughts = todayViewModel.uiState.collectAsState()

    todayViewModel.loadMood(sharedPreferences)

    var editingThought by remember { mutableStateOf<Thought?>(null) }
    var editedText by remember { mutableStateOf("") }

    if (editingThought != null) {
        AlertDialog(
            onDismissRequest = { editingThought = null },
            title = { Text("Edit Thought") },
            text = {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (user_id != null && editingThought != null) {
                        val currentThoughts = thoughts.value.morningThoughts.toMutableList()
                        val index = currentThoughts.indexOf(editingThought)
                        if (index != -1) {
                            currentThoughts[index] = editingThought!!.copy(thought = editedText)
                            val thoughtMap = mapOf("thoughts" to currentThoughts)
                            todayViewModel.db.collection(user_id).document("MorningThoughts").set(thoughtMap)
                        }
                    }
                    editingThought = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingThought = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionHeader(
                title = getDate(),
                subtitle = getGreeting()
            )
        }

        item {
            SanctuaryCard {
                Text(
                    text = "CURRENT STATS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "How does your spirit feel?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // Mood icons placeholder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MoodItem(
                        label = "Energetic",
                        icon = Icons.Default.WbSunny,
                        isSelected = currMood.value == "Energetic",
                        onClick = { todayViewModel.updateMood(sharedPreferences, "Energetic") }
                    )
                    MoodItem(
                        label = "Calm",
                        icon = Icons.Default.WaterDrop,
                        isSelected = currMood.value == "Calm",
                        onClick = { todayViewModel.updateMood(sharedPreferences, "Calm") }
                    )
                    MoodItem(
                        label = "Happy",
                        icon = Icons.Default.SentimentVerySatisfied,
                        isSelected = currMood.value == "Happy",
                        onClick = { todayViewModel.updateMood(sharedPreferences, "Happy") }
                    )
                    MoodItem(
                        label = "Anxious",
                        icon = Icons.Default.Waves,
                        isSelected = currMood.value == "Anxious",
                        onClick = { todayViewModel.updateMood(sharedPreferences, "Anxious") }
                    )
                    MoodItem(
                        label = "Tired",
                        icon = Icons.Default.NightsStay,
                        isSelected = currMood.value == "Tired",
                        onClick = { todayViewModel.updateMood(sharedPreferences, "Tired") }
                    )
                }
            }
        }

        item {
            Text(
                text = "VITALITY",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MetricCard("Steps", "0", Icons.Default.DirectionsWalk, modifier = Modifier.weight(1f))
                MetricCard("Water", "0L", Icons.Default.LocalDrink, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MetricCard("Sleep", "0h", Icons.Default.Bedtime, modifier = Modifier.weight(1f))
                MetricCard("Progress", "0%", Icons.Default.TrendingUp, modifier = Modifier.weight(1f))
            }
        }

        item {
            SanctuaryCard {
                Text(
                    text = "DAILY REMARKS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = dailyRemarks,
                    onValueChange = { dailyRemarks = it },
                    placeholder = { Text("Capture a morning thought...", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth()
                        .imePadding(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    )
                )
            }
        }


        item {
            Button(
                onClick = {
                    if (user_id != null && dailyRemarks.isNotBlank()) {
                        todayViewModel.saveMorningThought(user_id, thoughts.value.morningThoughts, dailyRemarks)
                        dailyRemarks = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save thought")
            }
        }

        item {
            if (thoughts.value.morningThoughts.isEmpty()) {
                Text("No thought saved till now", color = Color.Gray)
            }
        }

        val groupedThoughts = thoughts.value.morningThoughts
            .sortedByDescending { it.day }
            .groupBy { it.day }

        groupedThoughts.forEach { (day, thoughtsInDay) ->
            item {
                Row( horizontalArrangement = Arrangement.Center){
                    AssistChip(
                        onClick = { },
                        label = { Text(formatDayChip(day)) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            items(thoughtsInDay.size) { index ->
                val thought = thoughtsInDay[index]
                SanctuaryCard() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = thought.thought, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        }
                        Row {
                            IconButton(onClick = {
                                editingThought = thought
                                editedText = thought.thought
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = {
                                if (user_id != null) {
                                    todayViewModel.deleteMorningThought(user_id, thoughts.value.morningThoughts, thought)
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

}

private fun formatDayChip(day: String): String {
    return try {
        val today = java.time.LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val yesterday = java.time.LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
        when (day) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> {
                val date = java.time.LocalDate.parse(day)
                date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
            }
        }
    } catch (e: Exception) {
        day
    }
}

@Composable
fun MoodItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}
