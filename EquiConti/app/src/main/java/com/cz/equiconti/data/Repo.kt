package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repo "compat" minimale per consentire il build.
 * Rimpiazza/integra questi metodi con le vere chiamate al tuo DAO.
 */
object Repo {
    // TODO: aggiungi qui i metodi reali quando colleghi il DAO
}

/** Stub generici per evitare "Unresolved reference" su observeByOwner/listByOwner */
@Suppress("unused")
fun <T> observeByOwner(ownerId: Long): Flow<List<T>> = flow { emit(emptyList()) }

@Suppress("unused")
fun <T> listByOwner(ownerId: Long): List<T> = emptyList()
