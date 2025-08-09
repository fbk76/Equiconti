package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity delle transazioni.
 * NOTA: il tableName è "Txn" (con la T maiuscola) per combaciare
 * con le query già presenti nel tuo TxnDao (es. "FROM Txn").
 */
@Entity(
    tableName = "Txn",
    indices = [Index(value = ["ownerId"])]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,         // riferimento al cliente
    val date: Long,            // epoch millis
    val operation: String,     // es. "IN" / "OUT" o codice operazione
    val incomeCents: Long = 0, // entrate in centesimi
    val expenseCents: Long = 0 // uscite in centesimi
)
