package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {

    // READ
    @Query("SELECT * FROM horses WHERE ownerId = :ownerId ORDER BY name")
    fun getHorses(ownerId: Long): Flow<List<Horse>>

    @Query("SELECT * FROM horses WHERE id = :id")
    suspend fun getById(id: Long): Horse?

    // WRITE
    @Upsert
    suspend fun upsert(horse: Horse)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(horse: Horse): Long

    @Delete
    suspend fun delete(horse: Horse)
}
