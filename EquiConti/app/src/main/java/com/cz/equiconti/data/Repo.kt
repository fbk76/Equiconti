package com.cz.equiconti.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId

class Repo(private val db: EquiDb) {

    companion object {
        fun from(context: Context) = Repo(EquiDb.get(context))
    }

    /* =============== OWNER =============== */

    fun observeOwners(): Flow<List<Owner>> = db.ownerDao().observeAll()

    suspend fun listOwnersOnce(): List<Owner> =
        db.ownerDao().observeAll().first()

    suspend fun getOwnerById(id: Long): Owner? =
        db.ownerDao().getById(id)

    suspend fun upsertOwner(owner: Owner): Long =
        if (owner.id == 0L) db.ownerDao().insert(owner) else {
            db.ownerDao().update(owner); owner.id
        }

    suspend fun deleteOwner(owner: Owner) =
        db.ownerDao().delete(owner)

    /* =============== HORSE / TXN … come già sistemati =============== */

    fun dayBounds(date: LocalDate): Pair<Long, Long> {
        val start = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        return start to end
    }
}
