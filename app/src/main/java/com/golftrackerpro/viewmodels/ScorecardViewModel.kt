class ScorecardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GolfRepository(application)

    private val _currentRound = MutableLiveData<Round>()
    val currentRound: LiveData<Round> = _currentRound

    init {
        loadActiveRound()
    }

    private fun loadActiveRound() {
        viewModelScope.launch {
            _currentRound.value = repository.getActiveRound()
        }
    }

    fun updateScore(hole: Round.Hole, score: Int) {
        viewModelScope.launch {
            repository.updateHoleScore(_currentRound.value?.id ?: return@launch, hole.number, score)
            loadActiveRound()
        }
    }
}
