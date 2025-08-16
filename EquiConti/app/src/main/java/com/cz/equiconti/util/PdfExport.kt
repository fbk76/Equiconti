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
import kotlin.math.abs
import kotlin.math.max

private const val A4_WIDTH = 595  // punti (≈ 8.27")
private const val A4_HEIGHT = 842 // punti (≈ 11.7")

private fun formatEuro(cents: Long): String =
    "€ " + String.format("%.2f", cents / 100.0)

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
        if (y > A4_HEIGHT - 80) newPage("Report movimenti (continua)")

        // Con il tuo modello:
        // amountCents: positivo = ENTRATA, negativo = USCITA
        val income = max(0L, t.amountCents)
        val expense = max(0L, -t.amountCents)
        running += income - expense
        totIn += income
        totOut += expense

        val d = Instant.ofEpochMilli(t.timestamp).atZone(zone).toLocalDate().format(fmtDate)
        var cx = left
        canvas.drawText(d, cx, y, textPaint); cx += colDateW

        val opText = t.note?.takeIf { it.isNotBlank() } ?: "Operazione"
        val maxChars = 34
        val opClip = if (opText.length > maxChars) opText.take(maxChars - 1) + "…" else opText
        canvas.drawText(opClip, cx, y, textPaint); cx += colOpW

        canvas.drawText(formatEuro(income), cx, y, textPaint); cx += colInW
        canvas.drawText(formatEuro(expense), cx, y, textPaint); cx += colOutW
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
