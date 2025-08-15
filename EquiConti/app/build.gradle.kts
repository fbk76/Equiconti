plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Se usi Hilt lascia questa riga, altrimenti puoi rimuoverla
    // id("com.google.dagger.hilt.android")
    // id("kotlin-kapt")
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

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // niente di speciale
        }
    }

    buildFeatures {
        compose = true
        viewBinding = false
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE*"
            )
        }
    }
}

dependencies {
    // Core AndroidX
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Compose BOM per allineare le versioni
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

    // Navigation Compose (se la usi)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Hilt (solo se lo stai usando davvero)
    // implementation("com.google.dagger:hilt-android:2.51.1")
    // kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    // implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
