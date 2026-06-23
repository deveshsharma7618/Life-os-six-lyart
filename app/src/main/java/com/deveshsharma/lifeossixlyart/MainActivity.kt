package com.deveshsharma.lifeossixlyart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deveshsharma.lifeossixlyart.core.navigation.Screen
import com.deveshsharma.lifeossixlyart.features.auth.presentation.login.LoginScreen
import com.deveshsharma.lifeossixlyart.features.auth.presentation.signup.SignupScreen
import com.deveshsharma.lifeossixlyart.features.home.presentation.HomeScreen
import com.deveshsharma.lifeossixlyart.features.splash.presentation.SplashScreen
import com.deveshsharma.lifeossixlyart.ui.theme.LifeOsSixLyartTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeOsSixLyartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val nextScreen = if (auth.currentUser != null) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }
                    navController.navigate(nextScreen) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupClick = { email, password ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Signup.route) { inclusive = true }
                            }
                        }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
