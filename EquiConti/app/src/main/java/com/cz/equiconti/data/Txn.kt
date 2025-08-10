package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Txn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    val horseId: Long? = null,
    val dateMillis: Long,          // <— NOME COLONNA CORRETTO
    val incomeCents: Long = 0,
    val expenseCents: Long = 0,
    val note: String? = null
)
