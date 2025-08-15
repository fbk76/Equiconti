package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "txns")
data class Txn(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val horseId: Long,
    val amountCents: Long,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
