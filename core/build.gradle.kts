plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    configureAndroidModule()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(Deps.common.googleMaps)
    api(Deps.common.location)

    api(Deps.core.kotlin)
    api(Deps.common.coroutines)
    api(Deps.common.coroutinesPlayServices)
    api(Deps.common.coroutinesAndroid)

    api(Deps.common.liveData)
    api(Deps.common.liveDataSingleEvent)
    api(Deps.common.paging)

    api(Deps.core.dagger)
    kapt(Deps.core.daggerCompiler)
}
