package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.viewModel.MapsViewModel
import com.github.gibbrich.airmee.data.di.DataComponent
import com.github.gibbrich.airmee.ui.activity.MainActivity
import com.github.gibbrich.airmee.ui.fragment.ApartmentBookingFragment
import com.github.gibbrich.airmee.ui.fragment.ApartmentParametersFragment
import com.github.gibbrich.airmee.ui.fragment.MapsFragment
import com.github.gibbrich.airmee.viewModel.ApartmentParametersViewModel
import com.github.gibbrich.airmee.viewModel.ApartmentViewModel
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class
    ],
    dependencies = [
        DataComponent::class
    ]
)
interface AppComponent {
    fun inject(entry: MainActivity)

    fun inject(entry: MapsFragment)
    fun inject(entry: ApartmentBookingFragment)
    fun inject(entry: ApartmentParametersFragment)

    fun inject(entry: MapsViewModel)
    fun inject(entry: ApartmentParametersViewModel)
    fun inject(entry: ApartmentViewModel)
}