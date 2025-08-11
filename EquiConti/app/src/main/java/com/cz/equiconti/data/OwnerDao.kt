package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)

    // Stream continuo di tutti i proprietari, ordinati per cognome poi per PK
    @Query("SELECT * FROM Owner ORDER BY lastname, OwnerId")
    fun observeAll(): Flow<List<Owner>>

    // Lettura di un proprietario per PK (colonna OwnerId)
    @Query("SELECT * FROM Owner WHERE OwnerId = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?
}
