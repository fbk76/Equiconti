package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Transazione collegata a un Owner.
 * I nomi campo coincidono con quelli usati dalla UI: date, operation, incomeCents, expenseCents.
 */
@Entity(
    tableName = "txns",
    indices = [Index("ownerId")]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    /** timestamp in millis (epoch) */
    val date: Long,
    /** descrizione/causale */
    val operation: String,
    /** entrata in centesimi */
    val incomeCents: Long = 0,
    /** uscita in centesimi */
    val expenseCents: Long = 0
)
