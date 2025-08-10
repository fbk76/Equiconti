package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Txn",
    indices = [Index("ownerId"), Index("horseId"), Index("dateMillis")]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    val horseId: Long? = null,
    val dateMillis: Long,        // <-- importante
    val operation: String,
    val incomeCents: Long = 0,
    val expenseCents: Long = 0,
    val note: String? = null
)
