package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {

    // Flusso reattivo dei cavalli per proprietario
    @Query("SELECT * FROM horses WHERE ownerId = :ownerId ORDER BY name")
    fun getHorses(ownerId: Long): Flow<List<Horse>>

    // Inserisce o aggiorna (Room 2.6+)
    @Upsert
    suspend fun upsert(horse: Horse): Long

    @Delete
    suspend fun delete(horse: Horse)
}
