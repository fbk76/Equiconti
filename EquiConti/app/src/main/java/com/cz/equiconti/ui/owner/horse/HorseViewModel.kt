package com.cz.equiconti.ui.owner.horse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HorseViewModel @Inject constructor(
    private val repo: Repo // <-- lo userai quando collegherai Room
) : ViewModel() {

    /**
     * Aggiunge un cavallo per l'owner indicato.
     * Al momento fa solo log per non dipendere dal DB.
     */
    fun addHorse(ownerId: Long, name: String) {
        viewModelScope.launch {
            Log.d("HorseVM", "addHorse(ownerId=$ownerId, name=$name)")
            // TODO: collega al DB, es:
            // repo.addHorse(ownerId, name)
        }
    }

    /**
     * Aggiorna un cavallo esistente (placeholder).
     */
    fun updateHorse(horseId: Long, name: String) {
        viewModelScope.launch {
            Log.d("HorseVM", "updateHorse(horseId=$horseId, name=$name)")
            // TODO: collega al DB
        }
    }
}
