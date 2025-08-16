package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.HorseDao
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.OwnerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val ownerDao: OwnerDao,
    private val horseDao: HorseDao
) : ViewModel() {

    /** Proprietario singolo (può essere null se non esiste) */
    fun ownerFlow(ownerId: Long): Flow<Owner?> = ownerDao.getOwnerById(ownerId)

    /** Lista cavalli del proprietario */
    fun horses(ownerId: Long): Flow<List<Horse>> = horseDao.getHorses(ownerId)
}
