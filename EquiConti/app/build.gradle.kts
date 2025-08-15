// EquiConti/app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")                    // tienilo se usi Hilt/Room; altrimenti puoi rimuoverlo
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.cz.equiconti"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cz.equiconti"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures { compose = true }

    // Con Kotlin 2.0.x serve Compose Compiler 1.7.x
    composeOptions { kotlinCompilerExtensionVersion = "1.7.0" }

    kotlinOptions { jvmTarget = "17" }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose BOM compatibile con compiler 1.7.x
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.0")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material Components (per temi XML e compatibilità)
    implementation("com.google.android.material:material:1.12.0")

    // Hilt (rimuovi queste tre dipendenze e il plugin/kapt se non lo usi)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}
