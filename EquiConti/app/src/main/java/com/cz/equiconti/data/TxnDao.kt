package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {

    @Upsert
    suspend fun upsert(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    @Query("SELECT * FROM txns WHERE id = :txnId LIMIT 1")
    fun getById(txnId: Long): Flow<Txn?>

    // Tutte le transazioni dei cavalli appartenenti a un proprietario
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT *
        FROM txns
        WHERE horseId IN (SELECT id FROM horses WHERE ownerId = :ownerId)
        ORDER BY id DESC
    """)
    fun getByOwner(ownerId: Long): Flow<List<Txn>>
}
