package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeHorses(ownerId: Long): Flow<List<Horse>>

    @Insert
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}
