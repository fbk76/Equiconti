package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Txn",
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
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("ownerId"),
        Index("horseId")
    ]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val txnId: Long = 0L,
    val ownerId: Long,
    val horseId: Long? = null,
    val date: Date,
    val operation: String,
    val incomeCents: Long = 0,
    val expenseCents: Long = 0,
    val notes: String? = null
)
