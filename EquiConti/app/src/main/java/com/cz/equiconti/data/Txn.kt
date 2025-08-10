package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Txn",
    indices = [
        Index("ownerId"),
        Index("horseId"),
        Index("dateMillis")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Owner::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Horse::class,
            parentColumns = ["id"],
            childColumns = ["horseId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val txnId: Long = 0L,
    val ownerId: Long,
    val horseId: Long? = null,
    /** data della transazione in millisecondi (epoch) */
    val dateMillis: Long,
    /** descrizione/causale */
    val operation: String,
    /** entrate in centesimi */
    val incomeCents: Long = 0L,
    /** uscite in centesimi */
    val expenseCents: Long = 0L,
    val note: String? = null
)
