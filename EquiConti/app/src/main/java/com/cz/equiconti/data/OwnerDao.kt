package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)

    // elenco completo in tempo reale
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    // dettaglio per PK (la colonna si chiama **id**, come nel data class Owner)
    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?
}
