package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owner")
    suspend fun observeAll(): List<Owner>

    @Query("SELECT * FROM owner WHERE id = :id")
    suspend fun getById(id: Long): Owner?

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}
