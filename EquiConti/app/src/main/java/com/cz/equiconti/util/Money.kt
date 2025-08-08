package com.cz.equiconti.util

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(cents: Long?): String {
    val v = (cents ?: 0L).toDouble() / 100.0
    val fmt = NumberFormat.getCurrencyInstance(Locale.ITALY)
    return fmt.format(v)
}
