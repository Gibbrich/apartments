package com.github.gibbrich.airmee.data.di

import com.github.gibbrich.airmee.core.di.CoreComponent
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.core.manager.ResourceManager
import com.github.gibbrich.airmee.data.di.module.ApiModule
import com.github.gibbrich.airmee.data.di.module.DBModule
import com.github.gibbrich.airmee.data.di.module.DataModule
import dagger.Component

@DataScope
@Component(
    modules = [
        ApiModule::class,
        DBModule::class,
        DataModule::class
    ],
    dependencies = [
        CoreComponent::class
    ]
)
interface DataComponent {
    val apartmentsRepository: ApartmentsRepository
    val locationRepository: LocationRepository
    val resourceManager: ResourceManager
    val apartmentParametersRepository: ApartmentParametersRepository
}