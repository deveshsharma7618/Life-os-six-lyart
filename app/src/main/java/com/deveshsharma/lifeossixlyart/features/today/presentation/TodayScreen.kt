package com.deveshsharma.lifeossixlyart.features.today.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.deveshsharma.lifeossixlyart.core.presentation.components.SectionHeader
import com.deveshsharma.lifeossixlyart.features.home.presentation.HomeViewModel
import com.deveshsharma.lifeossixlyart.features.today.presentation.components.MetricCard
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
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
    val homeViewModel: HomeViewModel = viewModel()
    val user_id = Firebase.auth.currentUser?.uid
    var currMood = homeViewModel.moodState.collectAsState()
    var dailyRemarks by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val thoughts = homeViewModel.uiState.collectAsState()

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
                        onClick = { homeViewModel.updateMood(sharedPreferences, "Energetic") }
                    )
                    MoodItem(
                        label = "Calm",
                        icon = Icons.Default.WaterDrop,
                        isSelected = currMood.value == "Calm",
                        onClick = { homeViewModel.updateMood(sharedPreferences, "Calm") }
                    )
                    MoodItem(
                        label = "Happy",
                        icon = Icons.Default.SentimentVerySatisfied,
                        isSelected = currMood.value == "Happy",
                        onClick = { homeViewModel.updateMood(sharedPreferences, "Happy") }
                    )
                    MoodItem(
                        label = "Anxious",
                        icon = Icons.Default.Waves,
                        isSelected = currMood.value == "Anxious",
                        onClick = { homeViewModel.updateMood(sharedPreferences, "Anxious") }
                    )
                    MoodItem(
                        label = "Tired",
                        icon = Icons.Default.NightsStay,
                        isSelected = currMood.value == "Tired",
                        onClick = { homeViewModel.updateMood(sharedPreferences, "Tired") }
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
                        homeViewModel.saveMorningThought(user_id, thoughts.value.morningThoughts, dailyRemarks)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save thought")
            }
            Spacer( modifier = Modifier.height(150.dp))
        }

        item{
            if (thoughts.value.morningThoughts.isEmpty()){
                Text("No thought saved till now")
            }
        }

        items(thoughts.value.morningThoughts.size) {  index ->
            Column(
            ) {
                Text(thoughts.value.morningThoughts[index].thought)
                Text(thoughts.value.morningThoughts[index].day)
            }


        }

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
