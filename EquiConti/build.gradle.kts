plugins {
    // Android & Kotlin
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    // Compose plugin richiesto da Kotlin 2.0+
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
    // Hilt: DEVE avere la versione qui
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
