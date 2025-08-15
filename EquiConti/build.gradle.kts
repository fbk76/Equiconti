plugins {
    // Android Gradle Plugin
    id("com.android.application") version "8.4.2" apply false

    // Kotlin 2.0.20 allineato ovunque
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false

    // KSP per Room (versione accoppiata a Kotlin)
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false

    // Hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
