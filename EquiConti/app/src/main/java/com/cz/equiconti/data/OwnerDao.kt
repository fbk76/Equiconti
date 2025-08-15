package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {
    @Query("SELECT * FROM owner ORDER BY name")
    fun getOwners(): Flow<List<Owner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}
