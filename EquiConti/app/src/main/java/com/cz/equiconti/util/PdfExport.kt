package com.cz.equiconti.util

import android.content.Context
import android.graphics.Canvas
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

private const val A4_WIDTH = 595   // punti
private const val A4_HEIGHT = 842  // punti

/**
 * Esporta i movimenti in PDF e restituisce un Uri utilizzabile con Intent (VIEW/SHARE).
 *
 * ownerName:   "Cognome Nome"
 * horses:      elenco nomi cavalli
 * txns:        movimenti (già filtrati/ordinati come vuoi)
 * fromLabel:   "2025-08-01"
 * toLabel:     "2025-08-31"
 * fileName:    nome base file senza estensione
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

    val pdf = PdfDocument()
    var page = pdf.startPage(PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create())
    var canvas = page.canvas

    val left = 40f
    val top = 40f
    val lineGap = 16f

    var y = top

    fun drawHeader(c: Canvas, yStart: Float): Float {
        var yy = yStart
        var cx = left
        val colDateW = 80f
        val colOpW = 220f
        val colInW = 80f
        val colOutW = 80f
        c.drawText("Data", cx, yy, headerPaint); cx += colDateW
        c.drawText("Movimento", cx, yy, headerPaint); cx += colOpW
        c.drawText("Entrate", cx, yy, headerPaint); cx += colInW
        c.drawText("Uscite", cx, yy, headerPaint); cx += colOutW
        return yy + lineGap
    }

    fun newPage(title: String) {
        pdf.finishPage(page)
        page = pdf.startPage(PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pdf.pages.size + 1).create())
        canvas = page.canvas
        y = top
        canvas.drawText(title, left, y, titlePaint)
        y += lineGap * 1.5f
        y = drawHeader(canvas, y)
    }

    // Titolo iniziale
    canvas.drawText("Report movimenti", left, y, titlePaint); y += lineGap
    canvas.drawText("Proprietario: $ownerName", left, y, textPaint); y += lineGap
    if (horses.isNotEmpty()) {
        canvas.drawText("Cavalli: ${horses.joinToString(", ")}", left, y, textPaint); y += lineGap
    }
    canvas.drawText("Periodo: $fromLabel  –  $toLabel", left, y, textPaint); y += lineGap * 1.5f
    y = drawHeader(canvas, y)

    // colonne
    val colDateW = 80f
    val colOpW = 220f
    val colInW = 80f
    val colOutW = 80f

    var totIn = 0L
    var totOut = 0L

    txns.forEach { t ->
        if (y > A4_HEIGHT - 80) {
            newPage("Report movimenti (continua)")
        }

        val amount = t.amountCents
        val inc = max(amount, 0L)
        val out = max(-amount, 0L)
        totIn += inc
        totOut += out

        val dateStr = Instant.ofEpochMilli(t.timestamp).atZone(zone).toLocalDate().format(fmtDate)
        var cx = left
        canvas.drawText(dateStr, cx, y, textPaint); cx += colDateW

        val opText = t.note ?: ""
        val maxChars = 34
        val opClip = if (opText.length > maxChars) opText.take(maxChars - 1) + "…" else opText
        canvas.drawText(opClip, cx, y, textPaint); cx += colOpW

        canvas.drawText(formatEuro(inc), cx, y, textPaint); cx += colInW
        canvas.drawText(formatEuro(out), cx, y, textPaint)

        y += lineGap
    }

    // Totali
    y += lineGap
    canvas.drawText("Totale Entrate: ${formatEuro(totIn)}", left, y, headerPaint); y += lineGap
    canvas.drawText("Totale Uscite:  ${formatEuro(totOut)}", left, y, headerPaint); y += lineGap
    canvas.drawText("Saldo Finale:   ${formatEuro(totIn - totOut)}", left, y, headerPaint)

    pdf.finishPage(page)

    // Scrivi file
    val dir = context.getExternalFilesDir(null) ?: context.filesDir
    val outFile = File(dir, "$fileName.pdf")
    pdf.writeTo(FileOutputStream(outFile))
    pdf.close()

    // URI sicuro via FileProvider (assicurati del provider nel Manifest)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        outFile
    )
}

private fun formatEuro(cents: Long): String {
    val v = cents / 100.0
    return "€ " + String.format("%.2f", v)
}
