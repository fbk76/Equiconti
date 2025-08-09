package com.cz.equiconti

import android.app.Application
import android.os.Environment
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class EquiApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Se già impostato da qualcos'altro, lo sovrascrivo lo stesso (vogliamo il nostro logger)
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                writeCrashToFile(thread, throwable)
            } catch (_: Throwable) {
                // Nessun rethrow qui: vogliamo comunque lasciare che Android chiuda il processo
            } finally {
                // Lascia crashare normalmente (comportamento standard)
                // In alcuni device è meglio ri-terminare manualmente
                exitProcess(2)
            }
        }
    }

    private fun writeCrashToFile(thread: Thread, throwable: Throwable) {
        // Directory app-specific, non serve nessun permesso
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?: filesDir // fallback interno
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(dir, "equiconti_crash_$stamp.txt")

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        pw.println("=== EquiConti crash ===")
        pw.println("When: $stamp")
        pw.println("Thread: ${thread.name} (id=${thread.id})")
        pw.println("App version: 1.0.0")
        pw.println()
        throwable.printStackTrace(pw)
        pw.flush()

        file.writeText(sw.toString())
    }
}
