package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {

    // Tutti i cavalli di un proprietario
    @Query("SELECT * FROM horses WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getByOwner(ownerId: Long): Flow<List<Horse>>

    @Query("SELECT * FROM horses WHERE id = :horseId LIMIT 1")
    fun getById(horseId: Long): Flow<Horse?>

    @Upsert
    suspend fun upsert(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}
