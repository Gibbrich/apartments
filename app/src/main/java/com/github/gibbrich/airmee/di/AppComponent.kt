package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.ApartmentViewModel
import com.github.gibbrich.airmee.MainActivity
import com.github.gibbrich.airmee.MapsFragment
import com.github.gibbrich.airmee.viewModel.MapsViewModel
import com.github.gibbrich.airmee.data.di.DataComponent
import com.github.gibbrich.airmee.viewModel.ApartmentParametersViewModel
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
    fun inject(entry: MapsViewModel)
    fun inject(entry: ApartmentParametersViewModel)
    fun inject(entry: ApartmentViewModel)
}