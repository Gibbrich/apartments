object Deps {
    private const val kotlinVersion = "1.3.50"
    const val androidGradle = "com.android.tools.build:gradle:3.5.0"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val coroutines_android_version = "1.3.2"
    const val lifecycle_version = "2.1.0"

    object common {
        const val constraint = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
        const val arch = "androidx.arch.core:core-common:2.0.0"
        const val googleMaps = "com.google.android.gms:play-services-maps:17.0.0"
        const val location = "com.google.android.gms:play-services-location:17.0.0"
        const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
        const val vm = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val lifeCycle = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val liveDataSingleEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"
        const val recycler = "androidx.recyclerview:recyclerview:1.0.0"
        const val cardView = "androidx.cardview:cardview:1.0.0"
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val core = "androidx.core:core-ktx:1.2.0"
        const val material = "com.google.android.material:material:1.0.0"
        const val flexBoxLayout = "com.google.android:flexbox:2.0.1"
        const val paging = "androidx.paging:paging-runtime-ktx:2.1.1"

        const val glide = "com.github.bumptech.glide:glide:4.9.0"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3"
        const val coroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.3"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3"
    }

    object test {
        const val mockk = "io.mockk:mockk:1.9.3.kotlin12"
        const val junit = "junit:junit:4.12"
        const val androidTestRunner = "com.android.support.test:runner:1.0.2"
        const val androidTestRules = "com.android.support.test:rules:1.0.2"
        const val espresso = "androidx.test.espresso:espresso-core:3.1.0-alpha4"
        const val mockitoAndroid = "org.mockito:mockito-android:2.25.0"
        const val mockitoCore = "org.mockito:mockito-core:3.2.4"
        const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0"
        const val dexOpener = "com.github.tmurakami:dexopener:2.0.2"

        const val androidx_test_version = "1.2.0"
        const val runner = "androidx.test:runner:$androidx_test_version"
        const val rules = "androidx.test:rules:$androidx_test_version"

        //  Architecture Components testing libraries
        const val arch = "androidx.arch.core:core-testing:$lifecycle_version"

        // Coroutines testing
        const val coroutinesTesting = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_android_version"
    }

    object core {
        const val daggerVersion = "2.13"
        private const val rx2JavaVersion = "2.1.6"
        private const val rx2AndroidVersion = "2.0.1"

        const val rx2 = "io.reactivex.rxjava2:rxjava:$rx2JavaVersion"
        const val rx2Android = "io.reactivex.rxjava2:rxandroid:$rx2AndroidVersion"

        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    }


    object data {
        private const val okhttpVersion = "3.4.1"
        private const val retrofitVersion = "2.7.0"
        private const val gsonVersion = "2.7"
        private const val roomVersion = "2.2.0"

        const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        const val okhttpLoggingInteceptor = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val retrofitConverter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        const val gson = "com.google.code.gson:gson:$gsonVersion"
        const val room = "androidx.room:room-runtime:$roomVersion"
        const val roomCoroutines = "androidx.room:room-ktx:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    }
}