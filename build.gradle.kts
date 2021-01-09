// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
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

