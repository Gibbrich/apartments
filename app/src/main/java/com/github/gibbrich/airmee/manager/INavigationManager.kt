package com.github.gibbrich.airmee.manager

import androidx.navigation.NavController

interface INavigationManager {
    var navController: NavController?

    fun switchToApartmentsParametersScreen()
    fun switchToApartmentBookingScreen()
}