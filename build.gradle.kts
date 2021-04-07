// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version by extra("1.4.30")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath (Classpaths.gradlePlugin)
        classpath (Classpaths.kotlinGradlePlugin)
        classpath (Classpaths.navigationSafeArgs)
        classpath (Classpaths.googleServices)
        classpath (Classpaths.hilt)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}

