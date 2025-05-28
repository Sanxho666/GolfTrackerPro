// GolfCourse.kt
data class GolfCourse(
    val id: String,
    val name: String,
    val location: String,
    val holes: List<HoleInfo>
) {
    data class HoleInfo(
        val number: Int,
        val par: Int,
        val yardage: Int,
        val teeLatitude: Double,
        val teeLongitude: Double,
        val pinLatitude: Double,
        val pinLongitude: Double
    )
}
