package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.OwnerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersVm @Inject constructor(
    private val ownerDao: OwnerDao
) : ViewModel() {

    val owners = ownerDao
        .observeAll() // Flow<List<Owner>>
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addOwner(
        name: String,
        surname: String,
        phone: String?,
        onDone: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                ownerDao.insert(
                    Owner(
                        id = 0, // autoGenerate in @Entity
                        name = name,
                        surname = surname,
                        phone = phone
                    )
                )
                onDone()
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}
