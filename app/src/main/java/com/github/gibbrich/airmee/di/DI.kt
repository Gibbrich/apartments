package com.github.gibbrich.airmee.di

object DI {
    lateinit var appComponent: AppComponent
        private set

    fun init(appComponent: AppComponent) {
        DI.appComponent = appComponent
    }
}