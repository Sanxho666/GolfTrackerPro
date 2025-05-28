data class Round(
    val id: String,
    val courseId: String,
    val startTime: Long,
    val endTime: Long? = null,
    val players: List<Player>,
    val holes: List<Hole>,
    val weatherConditions: String? = null
) {
    fun getCurrentHole(): Hole {
        return holes.firstOrNull { it.score == null } ?: holes.last()
    }

    data class Player(
        val id: String,
        val name: String
    )

    data class Hole(
        val number: Int,
        val par: Int,
        val yardage: Int,
        var score: Int? = null,
        val shots: List<BallPosition> = emptyList(),
        val fairwayHit: Boolean? = null,
        val greensInRegulation: Boolean? = null,
        val putts: Int? = null
    )
}GolfCourse.kt
kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkRound
data class Round(
    val id: String,
    val courseId: String,
    val startTime: Long,
    val endTime: Long? = null,
    val players: List<Player>,
    val holes: List<Hole>,
    val weatherConditions: String? = null
) {
    fun getCurrentHole(): Hole {
        return holes.firstOrNull { it.score == null } ?: holes.last()
    }

    data class Player(
        val id: String,
        val name: String
    )

    data class Hole(
        val number: Int,
        val par: Int,
        val yardage: Int,
        var score: Int? = null,
        val shots: List<BallPosition> = emptyList(),
        val fairwayHit: Boolean? = null,
        val greensInRegulation: Boolean? = null,
        val putts: Int? = null
    )
} 
