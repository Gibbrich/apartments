plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    configureAndroidModule()
    defaultConfig {
        applicationId = "com.github.gibbrich.airmee"
//        testInstrumentationRunner = "com.github.gibbrich.airmee.MockTestRunner"
    }
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":data"))
    implementation(project(":core"))

    implementation(Deps.core.kotlin)

    implementation(Deps.common.appCompat)
    implementation(Deps.common.core)
    implementation(Deps.common.constraint)

    implementation(Deps.common.arch)

    implementation(Deps.common.legacy)
    implementation(Deps.common.vm)
    implementation(Deps.common.lifeCycle)
    implementation(Deps.common.recycler)
    implementation(Deps.common.flexBoxLayout)
    implementation(Deps.common.cardView)
    implementation(Deps.common.glide) {
        exclude("com.android.support")
    }

    implementation(Deps.common.coroutines)
    implementation(Deps.common.coroutinesAndroid)

    implementation(Deps.common.liveData)

    implementation(Deps.core.dagger)
    kapt(Deps.core.daggerCompiler)

    kapt(Deps.core.daggerCompiler)

    val nav_version_ktx = "2.1.0-alpha05"

    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version_ktx")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version_ktx")

    implementation(Deps.common.material)

    implementation(Deps.data.okhttp) {
        exclude(module = "okio")
    }

    implementation(Deps.data.okhttpLoggingInteceptor)
    implementation(Deps.data.retrofit)
    implementation(Deps.data.retrofitConverter)
    implementation(Deps.data.gson)

    implementation(Deps.data.room)
    implementation(Deps.data.roomCoroutines)

    androidTestImplementation(Deps.test.androidTestRunner)
    kaptAndroidTest(Deps.data.roomCompiler)

    testImplementation(Deps.test.junit)

    androidTestImplementation(Deps.test.androidTestRunner)
    androidTestImplementation(Deps.test.androidTestRules)
    androidTestImplementation(Deps.test.espresso)
    androidTestImplementation(Deps.test.dexOpener)
    androidTestImplementation(Deps.test.mockitoCore)
    androidTestImplementation(Deps.test.mockitoAndroid)
    androidTestImplementation(Deps.test.mockitoKotlin)
    androidTestImplementation(Deps.test.runner)
    androidTestImplementation(Deps.test.rules)
    androidTestImplementation(Deps.test.arch)
    androidTestImplementation(Deps.test.coroutinesTesting)
    kaptAndroidTest(Deps.core.daggerCompiler)
}
