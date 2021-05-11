// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath (Classpaths.gradlePlugin)
        classpath (Classpaths.kotlinGradlePlugin)
        classpath (Classpaths.navigationSafeArgs)
        classpath (Classpaths.googleServices)
        classpath (Classpaths.hilt)
        classpath (Classpaths.sql_delight)
        classpath (Classpaths.kotlinSerialization)

    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        maven(url = "https://kotlin.bintray.com/kotlinx/")
        mavenCentral()
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}

