package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {
    @Query("SELECT * FROM horses WHERE ownerId = :ownerId ORDER BY name")
    fun getOwnerHorses(ownerId: Long): Flow<List<Horse>>

    @Insert
    suspend fun insert(horse: Horse): Long
}
