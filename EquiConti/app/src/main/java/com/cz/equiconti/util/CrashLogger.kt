package com.cz.equiconti.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

object CrashLogger {

    @Volatile private var installed = false
    private const val FILE_PREFIX = "equiconti_crash_"

    fun install(context: Context) {
        if (installed) return
        installed = true
        val appContext = context.applicationContext

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            try {
                writeCrash(appContext, t, e)
            } catch (_: Throwable) {
            } finally {
                exitProcess(2)
            }
        }
        // breadcrumb: avvio processo
        writeBreadcrumb(appContext, "PROCESS START")
    }

    fun writeBreadcrumb(context: Context, text: String) {
        try {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val file = File(dir, "${FILE_PREFIX}breadcrumbs.txt")
            file.appendText("${timestamp()}  $text\n")
        } catch (_: Throwable) {}
    }

    private fun writeCrash(context: Context, thread: Thread, throwable: Throwable) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file = File(dir, "${FILE_PREFIX}${timestampForFile()}.txt")

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        pw.println("=== EquiConti crash ===")
        pw.println("When: ${timestamp()}")
        pw.println("Thread: ${thread.name} (id=${thread.id})")
        pw.println()
        throwable.printStackTrace(pw)
        pw.flush()

        file.writeText(sw.toString())
    }

    private fun timestamp(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(Date())

    private fun timestampForFile(): String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
}
