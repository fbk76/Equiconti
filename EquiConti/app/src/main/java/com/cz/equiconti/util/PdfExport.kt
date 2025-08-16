package com.cz.equiconti.util

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.cz.equiconti.data.Txn
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.max

private const val A4_WIDTH = 595  // punti (≈ 8.27")
private const val A4_HEIGHT = 842 // punti (≈ 11.7")

/* ========= Helpers reflection (niente dipendenze extra) ========= */

@Suppress("UNCHECKED_CAST")
private fun <T> Any.getFieldOrNull(vararg names: String): T? {
    val cls = this.javaClass
    for (n in names) {
        try {
            val f = cls.getDeclaredField(n)
            f.isAccessible = true
            val v = f.get(this) ?: continue
            return v as? T
        } catch (_: NoSuchFieldException) {
        } catch (_: SecurityException) {
        }
    }
    return null
}

private fun Txn.readDateMillis(): Long {
    // Prova una serie di nomi comuni
    return (getFieldOrNull<Number>("dateMillis", "date", "timeMillis", "timestamp")?.toLong())
        ?: 0L
}

private fun Txn.readOperation(): String {
    // Prova varianti comuni per la descrizione
    return getFieldOrNull<String>("operation", "desc", "description", "note")
        ?: ""
}

private fun Txn.readIncomeExpense(): Pair<Long, Long> {
    // Preferisci campi separati se esistono
    val inc = getFieldOrNull<Number>("incomeCents")?.toLong()
    val exp = getFieldOrNull<Number>("expenseCents")?.toLong()
    if (inc != null || exp != null) {
        return (inc ?: 0L) to (exp ?: 0L)
    }
    // In alternativa un unico importo con segno (amountCents)
    val amount = getFieldOrNull<Number>("amountCents", "amount", "valueCents")?.toLong() ?: 0L
    val income = max(0L, amount)
    val expense = max(0L, -amount)
    return income to expense
}

private fun formatEuro(cents: Long): String {
    val v = cents / 100.0
    return "€ " + String.format("%.2f", v)
}

/* ======================= Export PDF ======================= */

/**
 * Esporta i movimenti in PDF e restituisce un Uri per Intent (VIEW/SHARE).
 *
 * @param ownerName  "Cognome Nome"
 * @param horses     elenco nomi cavalli
 * @param txns       movimenti (già filtrati e ordinati per data crescente)
 * @param fromLabel  periodo "da" (es. 2025-08-01)
 * @param toLabel    periodo "a"  (es. 2025-08-31)
 * @param fileName   nome file senza estensione
 */
fun exportTxnsPdf(
    context: Context,
    ownerName: String,
    horses: List<String>,
    txns: List<Txn>,
    fromLabel: String,
    toLabel: String,
    fileName: String = "Report_Movimenti"
): Uri {
    val zone = ZoneId.systemDefault()
    val fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val pdf = PdfDocument()
    var page = pdf.startPage(PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create())
    var canvas = page.canvas

    val titlePaint = Paint().apply {
        isAntiAlias = true
        textSize = 16f
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
    }
    val headerPaint = Paint().apply {
        isAntiAlias = true
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
    }
    val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = 11f
    }

    val left = 40f
    var y = 40f
    val lineGap = 16f

    fun newPage(title: String? = null) {
        pdf.finishPage(page)
        val nextNum = pdf.pages.size + 1
        page = pdf.startPage(PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, nextNum).create())
        canvas = page.canvas
        y = 40f
        title?.let {
            canvas.drawText(it, left, y, titlePaint)
            y += lineGap * 1.5f
        }
        drawHeader()
    }

    // Titolo
    canvas.drawText("Report movimenti", left, y, titlePaint); y += lineGap
    canvas.drawText("Proprietario: $ownerName", left, y, textPaint); y += lineGap
    if (horses.isNotEmpty()) {
        canvas.drawText("Cavalli: ${horses.joinToString(", ")}", left, y, textPaint); y += lineGap
    }
    canvas.drawText("Periodo: $fromLabel  –  $toLabel", left, y, textPaint); y += lineGap * 1.5f

    // Intestazioni tabella
    val colDateW = 80f
    val colOpW   = 220f
    val colInW   = 80f
    val colOutW  = 80f
    val colBalW  = 80f

    fun drawHeader() {
        var cx = left
        canvas.drawText("Data", cx, y, headerPaint); cx += colDateW
        canvas.drawText("Movimento", cx, y, headerPaint); cx += colOpW
        canvas.drawText("Entrate", cx, y, headerPaint); cx += colInW
        canvas.drawText("Uscite", cx, y, headerPaint); cx += colOutW
        canvas.drawText("Saldo", cx, y, headerPaint)
        y += lineGap
    }

    drawHeader()

    // Righe + saldo progressivo
    var running = 0L
    var totIn = 0L
    var totOut = 0L

    txns.forEach { t ->
        if (y > A4_HEIGHT - 80) {
            newPage("Report movimenti (continua)")
        }

        val (inc, exp) = t.readIncomeExpense()
        running += inc - exp
        totIn += inc
        totOut += exp

        val d = Instant.ofEpochMilli(t.readDateMillis()).atZone(zone).toLocalDate().format(fmtDate)
        var cx = left
        canvas.drawText(d, cx, y, textPaint); cx += colDateW

        // tronca descrizione se troppo lunga
        val opText = t.readOperation()
        val maxChars = 34
        val opClip = if (opText.length > maxChars) opText.take(maxChars - 1) + "…" else opText
        canvas.drawText(opClip, cx, y, textPaint); cx += colOpW

        canvas.drawText(formatEuro(inc), cx, y, textPaint); cx += colInW
        canvas.drawText(formatEuro(exp), cx, y, textPaint); cx += colOutW
        canvas.drawText(formatEuro(running), cx, y, textPaint)

        y += lineGap
    }

    // Totali
    y += lineGap
    canvas.drawText("Totale Entrate: ${formatEuro(totIn)}", left, y, headerPaint); y += lineGap
    canvas.drawText("Totale Uscite:  ${formatEuro(totOut)}", left, y, headerPaint); y += lineGap
    canvas.drawText("Saldo Finale:   ${formatEuro(running)}", left, y, headerPaint)

    pdf.finishPage(page)

    // Scrivi file
    val dir = context.getExternalFilesDir(null) ?: context.filesDir
    val outFile = File(dir, "$fileName.pdf")
    pdf.writeTo(FileOutputStream(outFile))
    pdf.close()

    // URI sicuro via FileProvider
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        outFile
    )
}
