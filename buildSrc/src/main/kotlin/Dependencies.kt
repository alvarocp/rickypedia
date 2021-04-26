object Dependencies {

    object Libs {
        const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.Libs.retrofit2}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.Libs.retrofit2}"
        const val hilt = "com.google.dagger:hilt-android:${Versions.Libs.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.Libs.hilt}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.Libs.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.Libs.glide}"
        const val materialDialogs = "com.afollestad.material-dialogs:core:${Versions.Libs.material_dialogs}"
    }

    object Kotlin {
        const val jdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:" + Versions.Kotlin.kotlin
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}"
    }

    object AndroidX {
        const val material = "com.google.android.material:material:${Versions.AndroidX.material}"
        const val appCompat = "androidx.appcompat:appcompat:" + Versions.AndroidX.appCompat
        const val core = "androidx.core:core-ktx:" + Versions.AndroidX.core
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:" + Versions.AndroidX.constraintLayout
        const val legacySupport = "androidx.legacy:legacy-support-v4:" + Versions.AndroidX.legacySupport
        const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:" + Versions.AndroidX.coordinatorLayout
        const val lifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}"
        const val lifeCycleCore = "androidx.lifecycle:lifecycle-common-java8:${Versions.AndroidX.lifecycle}"
        const val lifeCycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.lifecycle}"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.navigation}"
        const val navigationRuntime = "androidx.navigation:navigation-runtime-ktx:${Versions.AndroidX.navigation}"
        const val cardView = "androidx.cardview:cardview:${Versions.AndroidX.cardview}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.AndroidX.recyclerview}"
        const val roomRuntime = "androidx.room:room-runtime:${Versions.AndroidX.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.AndroidX.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.AndroidX.room}"
        const val roomTesting = "androidx.room:room-testing:${Versions.AndroidX.room}"
    }

    object Test {
        const val junit = "junit:junit:" + Versions.Test.junit
        const val testExt = "androidx.test.ext:junit:" + Versions.Test.testExt
        const val espresso = "androidx.test.espresso:espresso-core:" + Versions.Test.espresso
        const val mockk = "io.mockk:mockk:" + Versions.Test.mockk
        const val mockkAndroid = "io.mockk:mockk-android:" + Versions.Test.mockk
        const val mockkCommon = "io.mockk:mockk-common:" + Versions.Test.mockk
        const val arch = "androidx.arch.core:core-testing:" + Versions.Test.arch
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Kotlin.coroutines}"
    }
}