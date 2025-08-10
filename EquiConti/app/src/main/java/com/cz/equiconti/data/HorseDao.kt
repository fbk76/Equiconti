package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)

    // Flow osservabile (usato da Repo)
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeByOwner(ownerId: Long): Flow<List<Horse>>

    // Se vuoi anche una funzione con lo stesso SQL ma nome diverso:
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun listByOwner(ownerId: Long): Flow<List<Horse>>

    @Query("SELECT * FROM Horse WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Horse?
}
