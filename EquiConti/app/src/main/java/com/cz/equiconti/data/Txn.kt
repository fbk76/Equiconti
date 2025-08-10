package com.cz.equiconti.data

import androidx.room.*

@Entity(
    tableName = "Txn",
    indices = [
        Index("ownerId"),
        Index("horseId")
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
    @PrimaryKey(autoGenerate = true) val txnId: Long = 0,
    val ownerId: Long,
    val horseId: Long? = null,
    /** data in millisecondi (UTC/local non importa, ma sia coerente ovunque) */
    val dateMillis: Long,
    val operation: String,
    val incomeCents: Long,
    val expenseCents: Long,
    val note: String? = null
)
