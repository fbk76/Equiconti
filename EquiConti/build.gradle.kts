// build.gradle.kts (ROOT)
plugins {
    id("com.android.application") version "8.4.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    // NECESSARIO con Kotlin 2.x + Compose
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false

    // Se usi Hilt, lascia, altrimenti puoi rimuoverlo
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
