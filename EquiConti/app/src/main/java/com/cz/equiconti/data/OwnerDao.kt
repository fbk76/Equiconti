package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(owner: Owner): Long

    @Query("SELECT * FROM owners ORDER BY name")
    fun all(): Flow<List<Owner>>
}
