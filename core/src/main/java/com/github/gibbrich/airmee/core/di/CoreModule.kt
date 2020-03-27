package com.github.gibbrich.airmee.core.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.repository.ApartmentParametersDataRepository
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.repository.ResourceDataManager
import com.github.gibbrich.airmee.core.repository.ResourceManager
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
    fun provideResourceManager(): ResourceManager = ResourceDataManager(context)

    @Provides
    @CoreScope
    fun provideApartmentParametersRepository(): ApartmentParametersRepository =
        ApartmentParametersDataRepository(
            MutableLiveData(0),
            MutableLiveData()
        )
}