package com.cz.equiconti.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "owners")
data class Owner(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String)
