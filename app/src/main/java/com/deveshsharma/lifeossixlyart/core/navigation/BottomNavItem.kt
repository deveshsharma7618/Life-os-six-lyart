package com.deveshsharma.lifeossixlyart.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Today : BottomNavItem("today", "Today", Icons.Default.CalendarToday)
    object Growth : BottomNavItem("growth", "Growth", Icons.Default.TrendingUp)
    object Diary : BottomNavItem("diary", "Diary", Icons.Default.MenuBook)
    object Archive : BottomNavItem("archive", "Archive", Icons.Default.History)
    object Memories : BottomNavItem("memories", "Memories", Icons.Default.PhotoLibrary)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}
