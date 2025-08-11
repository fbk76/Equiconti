package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Query("SELECT * FROM Owner ORDER BY lastName ASC")
    fun observeAll(): Flow<List<Owner>>

    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    fun getById(id: Long): Owner?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(owner: Owner)

    @Query("DELETE FROM Owner WHERE id = :id")
    suspend fun deleteById(id: Long)
}
