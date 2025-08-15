package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horses")
data class Horse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    val name: String
)
