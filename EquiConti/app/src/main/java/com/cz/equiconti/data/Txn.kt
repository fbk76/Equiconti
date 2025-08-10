// app/src/main/java/com/cz/equiconti/data/Txn.kt
package com.cz.equiconti.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Txn",
    foreignKeys = [
        ForeignKey(
            entity = Owner::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Horse::class,
            parentColumns = ["id"],
            childColumns = ["horseId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["ownerId"]),
        Index(value = ["horseId"]),
        Index(value = ["ownerId", "dateMillis"])
    ]
)
data class Txn(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "txnId")
    val id: Long = 0L,

    @ColumnInfo(name = "ownerId")
    val ownerId: Long,

    // opzionale: transazione legata a un cavallo
    @ColumnInfo(name = "horseId")
    val horseId: Long? = null,

    // data della transazione in millisecondi (epoch)
    @ColumnInfo(name = "dateMillis")
    val dateMillis: Long,

    // descrizione/causale
    @ColumnInfo(name = "operation")
    val operation: String,

    // importi in centesimi
    @ColumnInfo(name = "incomeCents")
    val incomeCents: Long = 0L,

    @ColumnInfo(name = "expenseCents")
    val expenseCents: Long = 0L,

    // nota libera opzionale
    @ColumnInfo(name = "note")
    val note: String? = null
)
