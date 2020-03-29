package com.github.gibbrich.airmee

import com.github.gibbrich.airmee.di.AppComponent
import com.github.gibbrich.airmee.di.AppModuleMock
import com.github.gibbrich.airmee.di.DaggerAppComponentMock

class AppTest : App() {
    override fun provideAppComponent(): AppComponent {
        return DaggerAppComponentMock.builder()
            .appModuleMock(AppModuleMock())
            .build()
    }
}