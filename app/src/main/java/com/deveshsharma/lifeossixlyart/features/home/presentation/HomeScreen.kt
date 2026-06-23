package com.deveshsharma.lifeossixlyart.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deveshsharma.lifeossixlyart.core.navigation.BottomNavItem
import com.deveshsharma.lifeossixlyart.core.navigation.Screen
import com.deveshsharma.lifeossixlyart.core.presentation.components.AppBackground
import com.deveshsharma.lifeossixlyart.features.archive.presentation.ArchiveScreen
import com.deveshsharma.lifeossixlyart.features.diary.presentation.DiaryScreen
import com.deveshsharma.lifeossixlyart.features.growth.presentation.GrowthScreen
import com.deveshsharma.lifeossixlyart.features.memories.presentation.MemoriesScreen
import com.deveshsharma.lifeossixlyart.features.profile.presentation.ProfileScreen
import com.deveshsharma.lifeossixlyart.features.today.presentation.TodayScreen
import com.deveshsharma.lifeossixlyart.ui.theme.SanctuaryGold
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val allItems = listOf(
        BottomNavItem.Today,
        BottomNavItem.Growth,
        BottomNavItem.Diary,
        BottomNavItem.Archive,
        BottomNavItem.Memories,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val currentTitle = allItems.find { it.route == currentDestination?.route }?.title ?: "Sanctuary"

    AppBackground {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color(0xFF151714),
                    modifier = Modifier.width(300.dp).fillMaxHeight().verticalScroll(
                        rememberScrollState()
                    ),
                    drawerShape = RoundedCornerShape(0.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 24.dp)) {
                        Text(
                            text = "My Journal",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Light,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        allItems.filter { it != BottomNavItem.Profile }.forEach { item ->
                            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                            NavigationDrawerItem(
                                label = { 
                                    Text(
                                        text = item.title.uppercase(),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            letterSpacing = 1.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                    ) 
                                },
                                selected = isSelected,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(item.icon, contentDescription = null) },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = Color(0xFF2A2A2A),
                                    unselectedContainerColor = Color.Transparent,
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = Color.Gray,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedTextColor = Color.Gray
                                ),
                                shape = RoundedCornerShape(0.dp),
                                modifier = if(isSelected) Modifier.leftBorder(2.dp, SanctuaryGold) else Modifier
                            )
                        }
                        

                        Column(
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                                StatItem(label = "MOOD", value = "■", valueColor = MaterialTheme.colorScheme.primary)
                                StatItem(label = "FOCUS", value = "—")
                                StatItem(label = "TASKS", value = "0 done", isItalic = true)
                            }
                        }

                        
                        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                        
                        // Profile at the bottom
                        val profileItem = BottomNavItem.Profile
                        val isProfileSelected = currentDestination?.hierarchy?.any { it.route == profileItem.route } == true
                        NavigationDrawerItem(
                            label = { Text(profileItem.title.uppercase()) },
                            selected = isProfileSelected,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(profileItem.route)
                            },
                            icon = { Icon(profileItem.icon, contentDescription = null) },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFF2A2A2A),
                                unselectedContainerColor = Color.Transparent,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(0.dp)
                        )
                    }
                }
            }
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = currentTitle,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(BottomNavItem.Diary.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        modifier = Modifier.padding(top = 64.dp)
                    ) {
                        Icon(BottomNavItem.Diary.icon, contentDescription = BottomNavItem.Diary.title)
                    }
                },
                bottomBar = {
                    Box(
                        modifier = Modifier.padding(10.dp).navigationBarsPadding()
                    ){
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.primary,
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                        ) {
                            val barItems = allItems.filter { it != BottomNavItem.Diary }

                            barItems.forEachIndexed { index, item ->
                                val isSelected = (currentDestination?.hierarchy?.any { it.route == item.route } == true)
                                val bgColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = Color.White,
                                        unselectedIconColor = Color.White,
                                        unselectedTextColor = Color.White,
                                        indicatorColor = Color.Transparent,
                                    ),
                                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(bgColor)
                                )
                            }
                    }

                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Today.route
                    ) {
                        composable(BottomNavItem.Today.route) { TodayScreen() }
                        composable(BottomNavItem.Growth.route) { GrowthScreen() }
                        composable(BottomNavItem.Diary.route) { DiaryScreen() }
                        composable(BottomNavItem.Archive.route) { ArchiveScreen() }
                        composable(BottomNavItem.Memories.route) { MemoriesScreen() }
                        composable(BottomNavItem.Profile.route) { ProfileScreen(
                            onLogOut = {
                                Firebase.auth.signOut()
                                navController.navigate(Screen.Login.route)
                            }
                        ) }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, valueColor: Color = Color.White, isItalic: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = valueColor,
                fontStyle = if (isItalic) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal,
                fontFamily = if (isItalic) FontFamily.Serif else FontFamily.Default
            )
        )
    }
}

// Source - https://stackoverflow.com/a/68595142
// Posted by Gabriele Mariotti, modified by community. See post 'Timeline' for change history
// Retrieved 2026-06-22, License - CC BY-SA 4.0

fun Modifier.leftBorder(
    strokeWidth: Dp,
    color: Color
) = composed {
    val strokeWidthPx = with(LocalDensity.current) {
        strokeWidth.toPx()
    }

    drawBehind {
        drawLine(
            color = color,
            start = Offset(strokeWidthPx / 2, 0f),
            end = Offset(strokeWidthPx / 2, size.height),
            strokeWidth = strokeWidthPx
        )
    }
}
