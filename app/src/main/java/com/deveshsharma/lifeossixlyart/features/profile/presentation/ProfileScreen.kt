package com.deveshsharma.lifeossixlyart.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryButton
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.deveshsharma.lifeossixlyart.core.presentation.components.SectionHeader

@Composable
fun ProfileScreen(onLogOut : ()-> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionHeader(
                title = "Manage your digital identity",
                subtitle = "Profile"
            )
        }

        item {
            SanctuaryCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFE5D5B7), Color(0xFFD4AF37))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("TE", style = MaterialTheme.typography.headlineLarge, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("test@gmail.com", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Joined June 22, 2026", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileMenuItem(text = "SETTINGS", icon = Icons.Default.Settings){

                }
                ProfileMenuItem(text = "LOG OUT", icon = Icons.Default.Logout, textColor = MaterialTheme.colorScheme.error){
                    onLogOut()
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("CURRENT STREAK", "0", "Days", modifier = Modifier.weight(1f))
                StatCard("TOTAL ENTRIES", "0", "", modifier = Modifier.weight(1f))
            }
        }

        item {
            SanctuaryCard(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "CURRENT PLAN",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Basic Sanctuary",
                        style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Serif),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Upgrade to Premium to unlock unlimited memories, detailed analytics, and custom sanctuary themes.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    SanctuaryButton(text = "UPGRADE PLAN", onClick = {})
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, textColor: Color = Color.White, onClick : () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White.copy(alpha = 0.05f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, style = MaterialTheme.typography.labelLarge, color = textColor)
            Icon(imageVector = icon, contentDescription = null, tint = textColor.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun StatCard(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    SanctuaryCard(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            if (unit.isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = unit, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}
