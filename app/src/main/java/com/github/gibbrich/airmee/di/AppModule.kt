package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.manager.INavigationManager
import com.github.gibbrich.airmee.manager.NavigationManager
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @AppScope
    @Provides
    fun provideNavigationManager(): INavigationManager = NavigationManager()
}