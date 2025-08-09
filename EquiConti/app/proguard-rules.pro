########################################
# Compose
########################################
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn androidx.compose.**

########################################
# Hilt / Dagger
########################################
-keep class dagger.hilt.** { *; }
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.**
-dontwarn javax.inject.**
-keep class **_HiltModules_* { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint { *; }

########################################
# AndroidX Hilt
########################################
-keep class androidx.hilt.** { *; }
-dontwarn androidx.hilt.**

########################################
# Room (prudenziale)
########################################
-keep class androidx.room.** { *; }
-keep class com.cz.equiconti.data.** { *; }
-dontwarn androidx.room.**

########################################
# WorkManager (prudenziale)
########################################
-keep class androidx.work.** { *; }
-dontwarn androidx.work.**

########################################
# Kotlinx Serialization
########################################
-keepattributes *Annotation*
-keepclassmembers class ** {
  @kotlinx.serialization.Serializable *;
}
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

########################################
# Non rimuovere Application/Activities/…
########################################
-keep class com.cz.equiconti.** extends android.app.Application { *; }
-keep class com.cz.equiconti.** extends android.app.Activity { *; }
-keep class com.cz.equiconti.** extends androidx.activity.ComponentActivity { *; }
-keep class com.cz.equiconti.** extends android.app.Service { *; }
-keep class com.cz.equiconti.** extends android.content.BroadcastReceiver { *; }
-keep class com.cz.equiconti.** extends android.content.ContentProvider { *; }
