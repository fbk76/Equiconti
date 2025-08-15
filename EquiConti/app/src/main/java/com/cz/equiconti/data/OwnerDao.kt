package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {
    @Query("SELECT * FROM owners ORDER BY name")
    fun getOwners(): Flow<List<Owner>>

    @Insert
    suspend fun insert(owner: Owner): Long

    @Query("SELECT * FROM owners WHERE id = :id")
    fun getOwner(id: Long): Flow<Owner?>
}
