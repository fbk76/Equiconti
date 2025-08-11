package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)

    /** Tutti i cavalli di un proprietario (stream) */
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeByOwner(ownerId: Long): Flow<List<Horse>>

    /** Alias utile se vuoi chiamarlo listByOwner dal Repo (sempre Flow) */
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun listByOwner(ownerId: Long): Flow<List<Horse>>

    @Query("SELECT * FROM Horse WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Horse?
}
