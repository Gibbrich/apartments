package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.ApartmentViewModel
import com.github.gibbrich.airmee.viewModel.MapsViewModel
import com.github.gibbrich.airmee.data.di.DataComponent
import com.github.gibbrich.airmee.viewModel.ApartmentParametersViewModel
import dagger.Component

@AppScope
@Component(
    dependencies = [
        DataComponent::class
    ]
)
interface AppComponent {
    fun inject(entry: MapsViewModel)
    fun inject(entry: ApartmentParametersViewModel)
    fun inject(entry: ApartmentViewModel)
}