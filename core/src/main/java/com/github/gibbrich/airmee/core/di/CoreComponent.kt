package com.github.gibbrich.airmee.core.di

import android.content.Context
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.repository.ResourceManager
import dagger.Component

@CoreScope
@Component(modules = [
    CoreModule::class
])
interface CoreComponent {
    val context: Context
    val resourceManager: ResourceManager
    // todo - think on implementation, maybe move to app
    val apartmentParametersRepository: ApartmentParametersRepository
}