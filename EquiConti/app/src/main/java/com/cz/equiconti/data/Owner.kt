package com.cz.equiconti.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Owner")
data class Owner(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val firstName: String,
    val lastName: String,
    val phone: String? = null
)
