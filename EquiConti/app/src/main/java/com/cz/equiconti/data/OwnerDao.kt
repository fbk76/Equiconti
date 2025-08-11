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

    // ← QUI la firma corretta: Flow e NON suspend
    @Query("SELECT * FROM Owner ORDER BY surname, name")
    fun observeAll(): Flow<List<Owner>>

    @Query("SELECT * FROM Owner WHERE ownerId = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?
}
