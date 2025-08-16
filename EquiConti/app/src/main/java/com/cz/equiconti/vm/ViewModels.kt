class OwnerDetailViewModel(
    private val repo: Repo
) : ViewModel() {

    // esempio: owner live data/flow
    val owner: StateFlow<Owner?> = repo.getOwners()
        .map { it.firstOrNull() } // o filtro per ID
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val horses: StateFlow<List<Horse>> = repo.getHorses(ownerId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addHorse(h: Horse) {
        viewModelScope.launch {
            repo.upsert(h)
        }
    }
}
