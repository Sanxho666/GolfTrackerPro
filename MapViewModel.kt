// MapViewModel.kt
class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GolfRepository(application)
    
    private val _currentCourse = MutableLiveData<GolfCourse>()
    val currentCourse: LiveData<GolfCourse> = _currentCourse
    
    private val _currentRound = MutableLiveData<Round>()
    val currentRound: LiveData<Round> = _currentRound
    
    fun loadCourse(courseId: String) {
        viewModelScope.launch {
            _currentCourse.value = repository.getCourseById(courseId)
        }
    }
    
    fun startNewRound(courseId: String, players: List<Round.Player>) {
        viewModelScope.launch {
            val course = repository.getCourseById(courseId)
            val holes = course.holes.map { holeInfo ->
                Round.Hole(
                    number = holeInfo.number,
                    par = holeInfo.par,
                    yardage = holeInfo.yardage
                )
            }
            
            val round = Round(
                id = UUID.randomUUID().toString(),
                courseId = courseId,
                startTime = System.currentTimeMillis(),
                players = players,
                holes = holes
            )
            
            repository.saveRound(round)
            _currentRound.value = round
        }
    }
    
    fun addShotToRound(roundId: String, ballPosition: BallPosition) {
        viewModelScope.launch {
            repository.addShotToRound(roundId, ballPosition)
            _currentRound.value = repository.getRoundById(roundId)
        }
    }
}

