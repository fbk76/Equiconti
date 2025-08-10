package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    indices = [Index("ownerId")]
)
data class Horse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val ownerId: Long,
    val name: String,
    /** quota mensile per cavallo in centesimi (es. 25000 = 250,00€) */
    val monthlyFeeCents: Long = 0L,
    val notes: String? = null
)
