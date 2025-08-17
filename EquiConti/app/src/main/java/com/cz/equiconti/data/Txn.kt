package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Movimento (transazione) associato a un cavallo.
 * - amountCents: importo in centesimi (positivo/negativo)
 * - notes: nota opzionale
 * - createdAt: timestamp di creazione (ms epoch)
 */
@Entity(tableName = "txns")
data class Txn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val horseId: Long,
    val amountCents: Long,
    val notes: String? = null,                 // <-- nuovo campo
    val createdAt: Long = System.currentTimeMillis()
)
