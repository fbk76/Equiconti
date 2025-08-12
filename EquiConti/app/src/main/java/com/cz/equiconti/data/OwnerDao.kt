package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)

    // Elenco osservabile (usato per la lista proprietari)
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    // Singolo proprietario per id (coerente con la tua entity: campo 'id')
    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getOwnerById(id: Long): Owner?
}
