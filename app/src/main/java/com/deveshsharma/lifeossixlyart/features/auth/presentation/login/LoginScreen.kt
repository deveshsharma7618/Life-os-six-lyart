package com.deveshsharma.lifeossixlyart.features.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deveshsharma.lifeossixlyart.core.presentation.components.AppBackground
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryButton
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryCard
import com.deveshsharma.lifeossixlyart.core.presentation.components.SanctuaryTextField

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AppBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SanctuaryCard(
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "My Journal",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Light
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "DIGITAL SANCTUARY",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 2.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    SanctuaryTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "EMAIL ADDRESS",
                        placeholder = "Enter your email",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SanctuaryTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "PASSWORD",
                        placeholder = "Enter your password",
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    SanctuaryButton(
                        text = "ENTER SANCTUARY",
                        onClick = { onLoginClick(email, password) }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Don't have a sanctuary? Create one here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToSignup() }
                    )
                }
            }
        }
    }
}
