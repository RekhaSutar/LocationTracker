package rekha.com.locationtracker.data

class Location {
    val latitude: Double
    val longitude: Double
    val accuracy: Float
    val time: Long

    constructor(latitude: Double, longitude: Double, accuracy: Float, time: Long) {
        this.latitude = latitude
        this.longitude = longitude
        this.accuracy = accuracy
        this.time = time
    }

}