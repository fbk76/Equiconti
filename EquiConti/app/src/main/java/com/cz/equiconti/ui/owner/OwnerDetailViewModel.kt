package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    fun owner(ownerId: Long): Flow<Owner?> = repo.getOwner(ownerId)

    fun horses(ownerId: Long): Flow<List<Horse>> = repo.getHorses(ownerId)
}
