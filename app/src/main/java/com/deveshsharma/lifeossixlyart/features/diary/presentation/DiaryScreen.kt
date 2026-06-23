package com.deveshsharma.lifeossixlyart.features.diary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryButton
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun DiaryScreen() {
    var diaryTitle by rememberSaveable { mutableStateOf("") }
    var diaryDesc by rememberSaveable { mutableStateOf("") }
    var diaryViewModel : DiaryViewModel  = viewModel()
    val userId = Firebase.auth.currentUser?.uid

    val diaries by diaryViewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SanctuaryCard(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = diaryTitle,
                onValueChange = { diaryTitle = it },
                placeholder = {
                    Text(
                        "Give this entry a title",
                        style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Serif),
                        color = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Serif),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            SanctuaryButton(
                text = "SAVE",
                onClick = {
                    diaryViewModel.saveDiary(diaryTitle, diaryDesc)
                    diaryTitle = ""
                    diaryDesc = ""
                })

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
                    .background(
                        Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp)
                    )
                    .defaultMinSize(minHeight = 150.dp)
            ) {
                TextField(
                    value = diaryDesc,
                    onValueChange = { diaryDesc = it },
                    placeholder = {
                        Text(
                            "Begin writing your thoughts...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 32.sp,
                                fontFamily = FontFamily.Serif
                            ),
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                        .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                        .imePadding(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 32.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {

            item {
                Text(
                    text = "Previous Entries",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val prevDiaries = diaries.diaryList.sortedByDescending { it.date }

            items(prevDiaries) { diary ->
                SanctuaryCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = diary.title, style = MaterialTheme.typography.headlineSmall)
                            Text(text = diary.desc, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = {
                            userId?.let { diaryViewModel.deleteDiary(diary) }
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Entry")
                        }
                    }
                }
            }

        }
    }

}
