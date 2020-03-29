package com.github.gibbrich.airmee.di

import com.github.gibbrich.airmee.viewModel.MapsViewModelTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModuleMock::class
])
interface AppComponentMock: AppComponent {
    fun inject(entry: MapsViewModelTest)
}