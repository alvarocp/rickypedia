plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Dependencies.Kotlin.jdk)

    // -- Coroutines
    implementation(Dependencies.Kotlin.coroutinesCore)
    testImplementation(Dependencies.Test.coroutinesTest)
}