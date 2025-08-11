package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class Repo(private val db: EquiDb) {
    // --- OWNER ---
    fun observeOwners(): Flow<List<Owner>> = db.ownerDao().observeAll()

    suspend fun listOwnersOnce(): List<Owner> =
        db.ownerDao().observeAll().first()

    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.id == 0L) {
            db.ownerDao().insert(owner)
        } else {
            db.ownerDao().update(owner); owner.id
        }
    }

    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)
}
