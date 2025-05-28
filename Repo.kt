class GolfRepository(context: Context) {
    private val dao = GolfDatabase.getDatabase(context).golfDao()
    
    suspend fun getCourseById(courseId: String): GolfCourse {
        return dao.getCourseById(courseId) ?: throw Exception("Course not found")
    }
    
    suspend fun getActiveRound(): Round? {
        return dao.getActiveRound()
    }
    
    suspend fun getRoundById(roundId: String): Round {
        return dao.getRoundById(roundId) ?: throw Exception("Round not found")
    }
    
    suspend fun saveRound(round: Round) {
        dao.insertRound(round)
    }
    
    suspend fun addShotToRound(roundId: String, ballPosition: BallPosition) {
        dao.addShotToRound(roundId, ballPosition)
    }
    
    suspend fun updateHoleScore(roundId: String, holeNumber: Int, score: Int) {
        dao.updateHoleScore(roundId, holeNumber, score)
    }
    
    suspend fun getRecentRounds(limit: Int = 10): List<Round> {
        return dao.getRecentRounds(limit)
    }
}