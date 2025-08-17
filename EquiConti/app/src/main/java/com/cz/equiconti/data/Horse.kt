package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horses")
data class Horse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val ownerId: Long,

    val name: String,

    /** Quota mensile in centesimi di euro (es: 5000 = 50,00 €) */
    val monthlyFeeCents: Long = 0L,

    /** Note libere sul cavallo */
    val notes: String? = null
)
