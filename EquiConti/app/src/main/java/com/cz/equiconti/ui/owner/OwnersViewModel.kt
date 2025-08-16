package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.HorseDao
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.OwnerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val ownerDao: OwnerDao,
    private val horseDao: HorseDao
) : ViewModel() {

    /** Lista completa dei proprietari (per la Home / lista) */
    fun ownersFlow(): Flow<List<Owner>> = ownerDao.getOwners()

    /** Dati del singolo proprietario (per OwnerDetailScreen) */
    fun ownerFlow(ownerId: Long): Flow<Owner?> =
        ownerDao.getOwners().map { owners -> owners.firstOrNull { it.id == ownerId } }

    /** Cavalli del proprietario (per OwnerDetailScreen) */
    fun horses(ownerId: Long): Flow<List<Horse>> = horseDao.getHorses(ownerId)

    /** Esempio di azione: aggiunta proprietario */
    fun addOwner(name: String, phone: String?) {
        viewModelScope.launch {
            ownerDao.insert(Owner(name = name, phone = phone))
        }
    }
}
