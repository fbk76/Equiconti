package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owners ORDER BY lastName ASC")
    fun getAllOwners(): Flow<List<Owner>>

    @Query("SELECT * FROM owners WHERE id = :id LIMIT 1")
    suspend fun getOwnerById(id: Long): Owner?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwner(owner: Owner): Long

    @Update
    suspend fun updateOwner(owner: Owner)

    @Delete
    suspend fun deleteOwner(owner: Owner)
}
