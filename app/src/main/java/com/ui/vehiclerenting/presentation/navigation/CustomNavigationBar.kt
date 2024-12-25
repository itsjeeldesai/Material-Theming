package com.ui.vehiclerenting.presentation.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ui.vehiclerenting.R
import com.ui.vehiclerenting.presentation.screens.CarDetailScreen
import com.ui.vehiclerenting.presentation.screens.FavouriteScreen
import com.ui.vehiclerenting.presentation.screens.HomeScreen
import com.ui.vehiclerenting.presentation.screens.MessageScreen
import com.ui.vehiclerenting.presentation.screens.ProfileScreen
import com.ui.vehiclerenting.presentation.screens.TripsScreen
import com.ui.vehiclerenting.presentation.viewmodels.AppSettingsViewModel
import com.ui.vehiclerenting.ui.theme.grayColorScheme
import com.ui.vehiclerenting.ui.theme.useAppColors


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CustomNavigationBar() {

    val navController = rememberNavController()
    val viewModel = viewModel<AppSettingsViewModel>()
    val context = LocalContext.current
    val window = (context as? Activity)?.window ?: return

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = remember { mutableStateOf(true) }
    val navigationBarColor = MaterialTheme.colorScheme.inverseSurface
    val defaultColor = Color.Transparent
    val notDarkTheme = !isSystemInDarkTheme()

    DisposableEffect(showBottomBar.value, currentRoute) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightNavigationBars = notDarkTheme

        window.navigationBarColor =
            if (showBottomBar.value) navigationBarColor.toArgb() else defaultColor.toArgb()

        onDispose {}
    }

    val menus = arrayOf(
        NavigationItems(painterResource(R.drawable.search), "Home", NavRoute.Home.name),
        NavigationItems(painterResource(R.drawable.favorite), "Favourite", NavRoute.Favourite.name),
        NavigationItems(painterResource(R.drawable.road), "Trips", NavRoute.Trips.name),
        NavigationItems(painterResource(R.drawable.message), "Message", NavRoute.Message.name),
        NavigationItems(painterResource(R.drawable.user), "User", NavRoute.Profile.name)
    )

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar.value,
                enter = slideInVertically(
                    animationSpec = tween(150),
                    initialOffsetY = { it }
                ),
                exit = shrinkVertically(
                    animationSpec = tween(150, easing = FastOutSlowInEasing)
                )
            ) {
                NavigationBar {
                    BottomBar(navController, menus)
                }
            }
        })
    { paddingValues ->
        NavGraph(navController, paddingValues, showBottomBar, viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    showBottomBar: MutableState<Boolean>,
    viewModel: AppSettingsViewModel
) {
    NavHost(navController = navController, startDestination = NavRoute.Home.route) {
        composable(NavRoute.Home.route) {
            showBottomBar.value = true
            HomeScreen(navController = navController, paddingValues =  paddingValues, viewModel = viewModel )
        }
        composable(NavRoute.Favourite.route) {
            showBottomBar.value = true
            FavouriteScreen(paddingValues)
        }
        composable(NavRoute.Trips.route) {
            showBottomBar.value = true
            TripsScreen(paddingValues)
        }
        composable(NavRoute.Message.route) {
            showBottomBar.value = true
            MessageScreen(paddingValues)
        }
        composable(NavRoute.Profile.route) {
            showBottomBar.value = true
            ProfileScreen(paddingValues)
        }
        composable(
            route = NavRoute.CarDetail.route,
            arguments = listOf(navArgument("carIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            showBottomBar.value = false
            val carIndex = backStackEntry.arguments?.getInt("carIndex") ?: 0
            CarDetailScreen(paddingValues, carIndex)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, menus: Array<NavigationItems>) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(currentRoute) {
        println("Current route: $currentRoute")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        menus.forEach { item ->
            key(item.route) {
                val isSelected = currentRoute == item.route.lowercase()
                GradientButton(
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = item.icon,
                    label = item.title,
                    isSelected = isSelected
                )
            }
        }
    }
}

@Composable
fun GradientButton(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val backgroundColor = if (isSelected) Brush.linearGradient(
        listOf(
            useAppColors(
                grayColors = grayColorScheme().primary,
                MaterialTheme.colorScheme.primary
            ),
            useAppColors(
                grayColors = grayColorScheme().secondary,
                MaterialTheme.colorScheme.inversePrimary
            )
        )
    ) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        label = "iconTint"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(backgroundColor, CircleShape)
                .border(1.dp, if (isSelected) Color.Transparent else Color.Gray, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}