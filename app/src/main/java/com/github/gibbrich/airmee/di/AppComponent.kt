package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.MapsViewModel
import com.github.gibbrich.airmee.data.di.DataComponent
import dagger.Component

@AppScope
@Component(
    dependencies = [
        DataComponent::class
    ]
)
interface AppComponent {
    fun inject(entry: MapsViewModel)
}