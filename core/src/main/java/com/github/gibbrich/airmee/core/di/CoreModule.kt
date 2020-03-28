package com.github.gibbrich.airmee.core.di

import android.content.Context
import com.github.gibbrich.airmee.core.repository.ApartmentParametersDataRepository
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.manager.ResourceDataManager
import com.github.gibbrich.airmee.core.manager.ResourceManager
import dagger.Module
import dagger.Provides

@Module
class CoreModule(
    private val context: Context
) {

    @Provides
    @CoreScope
    fun provideContext(): Context = context

    @Provides
    @CoreScope
    fun provideResourceManager(): ResourceManager =
        ResourceDataManager(context)

    @Provides
    @CoreScope
    fun provideApartmentParametersRepository(): ApartmentParametersRepository =
        ApartmentParametersDataRepository()
}