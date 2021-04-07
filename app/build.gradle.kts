plugins{
    id(Plugins.androidApplication)
    id(Plugins.googleServices)
    id(Plugins.kotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.kotlinParcelize)
    id(Plugins.kotlinApt)
    id(Plugins.safeArgs)
}

android {

    buildFeatures {
        viewBinding = true
    }

    compileSdkVersion(Config.compileSdk)
    buildToolsVersion(Config.buildTools)

    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName
        testInstrumentationRunner = Config.testInstrumentationRunner
    }

    buildTypes {
        buildTypes {
            getByName(Config.release){
                isMinifyEnabled = false
                isDebuggable = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.txt")
            }
            getByName(Config.debug){
                isDebuggable = true
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(
        fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))
    )

    implementation(Dependencies.Kotlin.jdk)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.AndroidX.legacySupport)
    implementation(Dependencies.AndroidX.coordinatorLayout)


    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.testExt)
    androidTestImplementation(Dependencies.Test.espresso)

    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.mockkAndroid)
    testImplementation(Dependencies.Test.mockkCommon)

    androidTestImplementation(Dependencies.Test.mockk)
    androidTestImplementation(Dependencies.Test.mockkAndroid)
    androidTestImplementation(Dependencies.Test.mockkCommon)


    //Instant task executor
    testImplementation(Dependencies.Test.arch)
    androidTestImplementation(Dependencies.Test.arch)


    implementation(Dependencies.Libs.retrofit2)
    implementation(Dependencies.Libs.retrofitGson)

    // -- Lifecycle Components (ViewModel, LiveData and ReactiveStreams)
    implementation(Dependencies.AndroidX.lifeCycleRuntime)
    implementation(Dependencies.AndroidX.lifeCycleCore)


    // -- Coroutines
    implementation(Dependencies.Kotlin.coroutinesCore)
    testImplementation(Dependencies.Test.coroutinesTest)
    androidTestImplementation(Dependencies.Test.coroutinesTest)


    // LiveData Coroutines
    implementation(Dependencies.AndroidX.lifeCycleLiveData)

    //HILT
    implementation(Dependencies.Libs.hilt)
    kapt(Dependencies.Libs.hiltCompiler)

    implementation(Dependencies.Libs.hiltViewModel)
    kapt(Dependencies.Libs.hiltViewModelCompiler)

    // jetpack navigation components
    implementation(Dependencies.AndroidX.navigationFragment)
    implementation(Dependencies.AndroidX.navigationUi)
    implementation(Dependencies.AndroidX.navigationRuntime)


    implementation(Dependencies.AndroidX.material)

    // material dialogs
    implementation(Dependencies.Libs.materialDialogs)


    // Card View
    implementation(Dependencies.AndroidX.cardView)

    // Recyclerview
    implementation(Dependencies.AndroidX.recyclerView)

//    //glide
    implementation(Dependencies.Libs.glide)
    annotationProcessor (Dependencies.Libs.glideCompiler)


    implementation(Dependencies.AndroidX.roomRuntime)
    kapt (Dependencies.AndroidX.roomCompiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(Dependencies.AndroidX.roomKtx)

    testImplementation(Dependencies.AndroidX.roomTesting)
    androidTestImplementation(Dependencies.AndroidX.roomTesting)


    implementation("com.google.firebase:firebase-analytics:18.0.0")

    implementation("com.google.firebase:firebase-auth:20.0.1")
}
