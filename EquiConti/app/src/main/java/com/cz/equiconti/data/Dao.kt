package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/* ===== OWNER ===== */
@Dao
interface OwnerDao {

    // Stream reattivo di tutti i proprietari ordinati per cognome/nome
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}

/* ===== HORSE ===== */
@Dao
interface HorseDao {

    // Stream dei cavalli di un proprietario
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeByOwner(ownerId: Long): Flow<List<Horse>>

    @Insert
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}
