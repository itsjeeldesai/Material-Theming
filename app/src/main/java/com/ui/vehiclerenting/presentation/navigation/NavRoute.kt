package com.ui.vehiclerenting.presentation.navigation

enum class NavRoute(val route: String) {
    Home("home"),
    Favourite("favourite"),
    Trips("trips"),
    Message("message"),
    Profile("profile"),
    CarDetail("car_detail/{carIndex}")
}