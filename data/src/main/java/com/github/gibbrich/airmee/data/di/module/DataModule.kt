package com.github.gibbrich.airmee.data.di.module

import android.content.Context
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.data.api.Api
import com.github.gibbrich.airmee.data.db.AppDatabase
import com.github.gibbrich.airmee.data.di.DataScope
import com.github.gibbrich.airmee.data.repository.ApartmentsDataRepository
import com.github.gibbrich.airmee.data.repository.LocationDataRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @DataScope
    fun provideCharactersRepository(
        api: Api,
        db: AppDatabase
    ): ApartmentsRepository = ApartmentsDataRepository(api, db)

    @Provides
    @DataScope
    fun provideLocationRepository(context: Context): LocationRepository = LocationDataRepository(context)
}