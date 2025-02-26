package com.example.chatwithai.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chatwithai.common.Constants.BottomNavItems

@Composable
fun NavBottomMenu(
    navController: NavController
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = Color.LightGray.copy(alpha = 0.5f)
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        BottomNavItems.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navItem.route) navItem.selectedIcon else navItem.unselectedIcon,
                        contentDescription = "Icon"
                    )
                },
                label = {
                    Text(text = navItem.label)
                },
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navItem.route) { saveState = true } // Delete all previous screens from stack
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}