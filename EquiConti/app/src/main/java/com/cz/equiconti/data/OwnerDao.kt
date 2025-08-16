package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    // READ
    @Query("SELECT * FROM owners ORDER BY name")
    fun getOwners(): Flow<List<Owner>>

    @Query("SELECT * FROM owners WHERE id = :id")
    suspend fun getById(id: Long): Owner?

    // WRITE
    @Upsert
    suspend fun upsert(owner: Owner)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(owner: Owner): Long

    @Delete
    suspend fun delete(owner: Owner)
}
