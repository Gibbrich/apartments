package com.github.gibbrich.airmee

import android.app.Application
import com.github.gibbrich.airmee.di.AppComponent
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.di.DaggerAppComponent
import com.github.gibbrich.airmee.core.di.DI as CoreDI
import com.github.gibbrich.airmee.data.di.DI as DataDI

open class App: Application() {
    override fun onCreate() {
        super.onCreate()

        CoreDI.init(this)
        DI.init(provideAppComponent())
    }

    /**
     * Override for implementing UI tests
     */
    open fun provideAppComponent(): AppComponent = DaggerAppComponent
        .builder()
        .dataComponent(DataDI.dataComponent)
        .build()

}