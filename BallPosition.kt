data class BallPosition(
    val latitude: Double,
    val longitude: Double,
    val holeNumber: Int,
    val shotNumber: Int,
    val clubUsed: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
