package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owners ORDER BY id DESC")
    fun getAll(): Flow<List<Owner>>

    @Query("SELECT * FROM owners WHERE id = :ownerId LIMIT 1")
    fun getById(ownerId: Long): Flow<Owner?>

    @Upsert
    suspend fun upsert(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}
