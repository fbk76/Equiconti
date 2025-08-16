package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owners ORDER BY name COLLATE NOCASE")
    fun getOwners(): Flow<List<Owner>>

    @Query("SELECT * FROM owners WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<Owner?>

    @Query("SELECT * FROM owners WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?

    @Upsert
    suspend fun upsert(owner: Owner): Long

    @Delete
    suspend fun delete(owner: Owner)
}
