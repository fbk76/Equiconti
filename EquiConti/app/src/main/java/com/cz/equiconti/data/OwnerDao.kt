package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owners ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    @Query("SELECT * FROM owners WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<Owner?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}
