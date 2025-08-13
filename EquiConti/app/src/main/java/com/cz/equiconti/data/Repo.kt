// 1) Repo.kt — compat layer per far compilare il progetto ora.
//    Sostituisci con la tua implementazione reale quando colleghi il DAO.

package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repo "compat" minimale per consentire il build.
 * Rimpiazza/integra questi metodi con le vere chiamate al tuo DAO.
 */
object Repo {
    // Esempi di placeholder: aggiungi qui metodi reali quando pronti.
    // fun getOwners(): Flow<List<Owner>> = ...
    // fun insertOwner(owner: Owner) = ...
}

/**
 * Stub generici per evitare "Unresolved reference" su observeByOwner/listByOwner
 * Usano Flow vuoti o liste vuote, così la compilazione passa.
 * Sostituiscili con implementazioni vere collegate al tuo DAO.
 */
@Suppress("unused")
fun <T> observeByOwner(ownerId: Long): Flow<List<T>> = flow { emit(emptyList()) }

@Suppress("unused")
fun <T> listByOwner(ownerId: Long): List<T> = emptyList()
