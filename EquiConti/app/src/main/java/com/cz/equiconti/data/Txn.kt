package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Transazione contabile.
 * La data è salvata in epoch millis (Long) per evitare TypeConverter.
 */
@Entity(
    tableName = "Txn",
    indices = [
        Index(value = ["ownerId"]),
        Index(value = ["horseId"])
    ]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val txnId: Long = 0L,

    val ownerId: Long,          // FK logica verso Owner.id
    val horseId: Long? = null,  // FK logica verso Horse.id (opzionale)

    val dateMillis: Long,       // <-- sostituisce java.util.Date
    val operation: String,      // es. "Lezione", "Pensione", ecc.

    val incomeCents: Long = 0L,   // entrata in centesimi
    val expenseCents: Long = 0L,  // uscita in centesimi

    val notes: String? = null
)
