plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinApt)
    id(Plugins.kotlinParcelize)

}

android {
    compileSdkVersion(Config.compileSdk)
    buildToolsVersion(Config.buildTools)


    defaultConfig {
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
    implementation(project(":shared"))

    implementation(Dependencies.Kotlin.jdk)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.core)

    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.mockkAndroid)
    testImplementation(Dependencies.Test.mockkCommon)

    androidTestImplementation(Dependencies.Test.mockk)
    androidTestImplementation(Dependencies.Test.mockkAndroid)
    androidTestImplementation(Dependencies.Test.mockkCommon)
    //Instant task executor
    testImplementation(Dependencies.Test.arch)
    androidTestImplementation(Dependencies.Test.arch)


    implementation(Dependencies.AndroidX.material)

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.testExt)
    androidTestImplementation(Dependencies.Test.espresso)

    implementation(Dependencies.Libs.retrofit2)
    implementation(Dependencies.Libs.retrofitGson)


    implementation(Dependencies.AndroidX.roomRuntime)
    kapt (Dependencies.AndroidX.roomCompiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(Dependencies.AndroidX.roomKtx)

    testImplementation(Dependencies.AndroidX.roomTesting)
    androidTestImplementation(Dependencies.AndroidX.roomTesting)

    // -- Coroutines
    implementation(Dependencies.Kotlin.coroutinesCore)
    testImplementation(Dependencies.Test.coroutinesTest)
    androidTestImplementation(Dependencies.Test.coroutinesTest)

    //HILT
    implementation(Dependencies.Libs.hilt)
    kapt(Dependencies.Libs.hiltCompiler)

}