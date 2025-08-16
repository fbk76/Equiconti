plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Compose compiler integrato con Kotlin 2.x
    id("com.google.devtools.ksp")             // Room compiler via KSP
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

    // JVM 17 (coerente con AGP/Kotlin recenti)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        // Evita conflitti di licenze comuni
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // --- Compose BOM (gestisce in blocco le versioni Compose) ---
    val composeBom = platform("androidx.compose:compose-bom:2024.08.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose UI + Material3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Tooling/debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Activity Compose & KTX di base
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")

    // Lifecycle / ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // Navigation (Compose)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Material Components (fornisce i temi XML Theme.Material3.*)
    implementation("com.google.android.material:material:1.12.0")

    // Room + KSP
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // WorkManager (se ti serve)
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
