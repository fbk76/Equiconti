// EquiConti/build.gradle.kts
plugins {
    // Versioni dei plugin definite a livello di root
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false // rimuovi se non usi Hilt
}
