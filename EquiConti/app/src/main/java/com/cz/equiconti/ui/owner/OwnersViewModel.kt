package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    // Lista osservabile di tutti i proprietari (per la schermata elenco)
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ---- Gestione form (Add/Edit Owner)
    private val _form = MutableStateFlow(OwnerFormState())
    val form: StateFlow<OwnerFormState> = _form.asStateFlow()

    fun startEdit(owner: Owner?) {
        _form.value = if (owner == null) {
            OwnerFormState()
        } else {
            OwnerFormState(
                id = owner.id,
                firstName = owner.firstName,
                lastName = owner.lastName,
                phone = owner.phone.orEmpty()
            )
        }
    }

    fun editFirstName(value: String) = _form.update { it.copy(firstName = value) }
    fun editLastName(value: String)  = _form.update { it.copy(lastName = value) }
    fun editPhone(value: String)     = _form.update { it.copy(phone = value) }

    /** Salva lo stato corrente del form (insert/update) */
    fun save(onSaved: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repo.upsertOwner(_form.value.toOwner())
            onSaved(id)
        }
    }

    // ---- Operazioni dirette (se preferisci passare Owner già pronto)
    fun upsertOwner(owner: Owner) {
        viewModelScope.launch { repo.upsertOwner(owner) }
    }

    fun deleteOwner(owner: Owner) {
        viewModelScope.launch { repo.deleteOwner(owner) }
    }

    suspend fun getOwnerById(id: Long): Owner? = repo.getOwnerById(id)
}

/** Stato UI per il form di proprietario */
data class OwnerFormState(
    val id: Long = 0L,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = ""
) {
    fun toOwner(): Owner = Owner(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phone = phone.ifBlank { null }
    )
}
