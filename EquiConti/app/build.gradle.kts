plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'com.google.dagger.hilt.android'
}

android {
    namespace 'com.cz.equiconti'
    compileSdk 34

    defaultConfig {
        applicationId "com.cz.equiconti"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildFeatures { compose true }
    composeOptions { kotlinCompilerExtensionVersion "1.5.14" }
    kotlinOptions { jvmTarget = '17' }

    packaging { resources { excludes += '/META-INF/{AL2.0,LGPL2.1}' } }
}

dependencies {
    implementation platform('androidx.compose:compose-bom:2024.06.00')
    androidTestImplementation platform('androidx.compose:compose-bom:2024.06.00')

    implementation 'androidx.activity:activity-compose:1.9.0'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    debugImplementation 'androidx.compose.ui:ui-tooling'

    implementation 'androidx.navigation:navigation-compose:2.7.7'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.3'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3'

    implementation 'com.google.dagger:hilt-android:2.51.1'
    kapt 'com.google.dagger:hilt-android-compiler:2.51.1'
    implementation 'androidx.hilt:hilt-navigation-compose:1.2.0'

    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'

    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1'
    implementation 'androidx.compose.material:material-icons-extended'
}
