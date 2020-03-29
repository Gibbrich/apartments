package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.core.manager.ResourceManager
import com.github.gibbrich.airmee.core.repository.ApartmentFiltersRepository
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.manager.INavigationManager
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class AppModuleMock {
    @Provides
    @Singleton
    fun provideApartmentsRepository(): ApartmentsRepository =
        Mockito.mock(ApartmentsRepository::class.java)

    @Provides
    @Singleton
    fun provideLocationRepository(): LocationRepository =
        Mockito.mock(LocationRepository::class.java)

    @Provides
    @Singleton
    fun provideApartmentFiltersRepository(): ApartmentFiltersRepository =
        Mockito.mock(ApartmentFiltersRepository::class.java)

    @Provides
    @Singleton
    fun provideNavigationManager(): INavigationManager = Mockito.mock(INavigationManager::class.java)

    @Provides
    @Singleton
    fun provideResourceManager(): ResourceManager = Mockito.mock(ResourceManager::class.java)
}