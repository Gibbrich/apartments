package com.github.gibbrich.airmee.manager

import androidx.navigation.NavController
import com.github.gibbrich.airmee.ui.fragment.ApartmentBookingFragment
import com.github.gibbrich.airmee.R

interface INavigationManager {
    var navController: NavController?

    fun switchToApartmentsParametersScreen()
    fun switchToApartmentBookingScreen(apartmentId: Int)
    fun exit()
}

class NavigationManager: INavigationManager {
    override var navController: NavController? = null

    override fun switchToApartmentsParametersScreen() {
        navController?.let {
            if (it.currentDestination?.id == R.id.mapsFragment) {
                it.navigate(R.id.action_mapsFragment_to_apartmentParametersFragment)
            }
        }
    }

    override fun switchToApartmentBookingScreen(apartmentId: Int) {
        navController?.let {
            if (it.currentDestination?.id == R.id.mapsFragment) {
                it.navigate(
                    R.id.action_mapsFragment_to_apartmentBookingFragment,
                    ApartmentBookingFragment.getParams(apartmentId)
                )
            }
        }
    }

    override fun exit() {
        navController?.popBackStack()
    }
}