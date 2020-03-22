import java.net.URI

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    configureAndroidModule()
}

repositories {
    maven { url = URI("https://kotlin.bintray.com/kotlinx/") }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))

    implementation(Deps.data.okhttp) {
        exclude(module = "okio")
    }

    implementation(Deps.data.okhttpLoggingInteceptor)
    implementation(Deps.data.retrofit)
    implementation(Deps.data.retrofitConverter)
    implementation(Deps.data.gson)

    implementation(Deps.data.room)
    implementation(Deps.data.roomCoroutines)
    kapt(Deps.data.roomCompiler)

    kapt(Deps.core.daggerCompiler)

    testImplementation(Deps.test.junit)
    testImplementation(Deps.test.mockk)

    androidTestImplementation(Deps.test.androidTestRunner)
    kaptAndroidTest(Deps.data.roomCompiler)
}
