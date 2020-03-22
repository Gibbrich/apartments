package com.github.gibbrich.airmee.core.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class CoreModule(
    private val context: Context
) {

    @Provides
    @CoreScope
    fun provideContext(): Context = context
}