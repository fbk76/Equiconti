package com.cz.equiconti.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entità Cavallo.
 * Collega ogni cavallo a un Owner tramite ownerId (FK verso Owner.id).
 * Aggiungo monthlyFeeCents per la quota fissa mensile per cavallo.
 */
@Entity(
    tableName = "Horse",
    foreignKeys = [
        ForeignKey(
            entity = Owner::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ownerId"])]
)
data class Horse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val ownerId: Long,
    @NonNull val name: String,
    val breed: String? = null,
    /** Quota fissa mensile del cavallo in centesimi (es. 25000 = 250,00€) */
    val monthlyFeeCents: Long = 0L,
    val note: String? = null
)
