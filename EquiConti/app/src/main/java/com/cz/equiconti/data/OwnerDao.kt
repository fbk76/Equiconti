package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    // Usato da Repo.observeOwners() -> ownerDao().observeAll()
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    // Usato da Repo.getOwnerById(id)
    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}
