plugins {
    id("com.android.application") version "8.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "1.9.24" apply false

    // KSP allineato alla versione Kotlin
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false

    // Hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
