package com.github.gibbrich.airmee.data.di

import com.github.gibbrich.airmee.core.di.DI as CoreDI


object DI {
    val dataComponent: DataComponent by lazy {
        DaggerDataComponent
            .builder()
            .coreComponent(CoreDI.coreComponent)
            .build()
    }
}