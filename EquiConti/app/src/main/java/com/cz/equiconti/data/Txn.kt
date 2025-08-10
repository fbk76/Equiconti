// app/src/main/java/com/cz/equiconti/data/Txn.kt
package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    ]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, // ← NON txnId
    val ownerId: Long,
    val horseId: Long? = null,
    val date: Long,             // epoch millis
    val operation: String,      // “income” | “expense” (o come preferisci)
    val incomeCents: Long = 0L,
    val expenseCents: Long = 0L,
    val note: String? = null
)
