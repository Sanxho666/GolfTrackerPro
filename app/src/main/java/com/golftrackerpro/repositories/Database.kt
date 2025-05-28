@Database(
    entities = [GolfCourse::class, Round::class, BallPosition::class],
    version = 1,
    exportSchema = false
)
abstract class GolfDatabase : RoomDatabase() {
    abstract fun golfDao(): GolfDao
    
    companion object {
        @Volatile
        private var INSTANCE: GolfDatabase? = null
        
        fun getDatabase(context: Context): GolfDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GolfDatabase::class.java,
                    "golf_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface GolfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: GolfCourse)
    
    @Query("SELECT * FROM GolfCourse WHERE id = :courseId")
    suspend fun getCourseById(courseId: String): GolfCourse?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRound(round: Round)
    
    @Query("SELECT * FROM Round WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveRound(): Round?
    
    @Query("SELECT * FROM Round WHERE id = :roundId")
    suspend fun getRoundById(roundId: String): Round?
    
    @Insert
    suspend fun addShotToRound(ballPosition: BallPosition)
    
    @Query("UPDATE Round SET holes = :holes WHERE id = :roundId")
    suspend fun updateRoundHoles(roundId: String, holes: List<Round.Hole>)
    
    @Query("SELECT * FROM Round ORDER BY startTime DESC LIMIT :limit")
    suspend fun getRecentRounds(limit: Int): List<Round>
    
    @Transaction
    suspend fun updateHoleScore(roundId: String, holeNumber: Int, score: Int) {
        val round = getRoundById(roundId) ?: return
        val updatedHoles = round.holes.map { hole ->
            if (hole.number == holeNumber) {
                hole.copy(score = score)
            } else {
                hole
            }
        }
        updateRoundHoles(roundId, updatedHoles)
    }
}