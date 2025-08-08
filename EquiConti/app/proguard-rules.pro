# Keep annotations (Room/Hilt/Kotlin)
-keepattributes *Annotation*

# ---------- ROOM ----------
# Tieni il database e le entità Room
-keep class ** extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep class androidx.room.** { *; }

# ---------- HILT / DI ----------
# Hilt aggiunge già consumer-rules, ma queste aiutano
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# ---------- WORKMANAGER ----------
# Mantieni i Worker se usati via riflessione
-keep class ** implements androidx.work.ListenableWorker { *; }
-keep class androidx.hilt.work.HiltWorkerFactory { *; }

# ---------- KOTLIN ----------
-keep class kotlin.** { *; }
-keepclassmembers class kotlin.Metadata { *; }
