package com.deveshsharma.lifeossixlyart.features.diary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryButton
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard

@Composable
fun DiaryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SanctuaryCard(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { 
                    Text(
                        "Give this entry a title", 
                        style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Serif),
                        color = Color.Gray
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SanctuaryButton(text = "SAVE", onClick = {})
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "MONDAY, JUNE 22, 2026",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Lined paper effect placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Begin writing your thoughts...",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 32.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color.Gray
                )
            }
        }
    }
}
