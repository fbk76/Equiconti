package com.cz.equiconti.data

import androidx.room.*
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Entity
data class Owner(
    @PrimaryKey(autoGenerate = true) val ownerId: Long = 0,
    val surname: String,
    val name: String,
    val phone: String? = null,
    val notes: String? = null
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Owner::class,
        parentColumns = ["ownerId"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("ownerId")]
)
data class Horse(
    @PrimaryKey(autoGenerate = true) val horseId: Long = 0,
    val ownerId: Long,
    val name: String,
    val monthlyFeeCents: Long = 0
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Owner::class,
        parentColumns = ["ownerId"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("ownerId"), Index("date")]
)
data class Txn(
    @PrimaryKey(autoGenerate = true) val txnId: Long = 0,
    val ownerId: Long,
    val date: String, // ISO yyyy-MM-dd
    val operation: String,
    val incomeCents: Long = 0,
    val expenseCents: Long = 0
)

data class OwnerWithHorses(
    @Embedded val owner: Owner,
    @Relation(parentColumn = "ownerId", entityColumn = "ownerId")
    val horses: List<Horse>
)
