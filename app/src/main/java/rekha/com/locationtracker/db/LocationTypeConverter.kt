package rekha.com.locationtracker.db

import android.arch.persistence.room.TypeConverter

import org.json.JSONArray
import org.json.JSONObject

import rekha.com.locationtracker.data.Location
import java.math.BigDecimal

class LocationTypeConverter {

    val latitude = "latitude"
    val longitude = "longitude"
    val accuracy = "accuracy"
    val time = "time"

    @TypeConverter
    fun toJsonArrayString(locations: List<Location>): String {
        var jsonArray : JSONArray = JSONArray()
        locations.map { it->
            var jsonObj = JSONObject()
            jsonObj.put(latitude, it.latitude)
            jsonObj.put(longitude, it.longitude)
            jsonObj.put(accuracy, it.accuracy)
            jsonObj.put(time, it.time)

            jsonArray.put(jsonObj)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toLocation(jsonArrayString: String): List<Location> {
        var locations: MutableList<Location> = arrayListOf()
        var jsonArray = JSONArray(jsonArrayString)
        var i : Int = 0
        while (i <= jsonArray.length() - 1){
            var jsonObj: JSONObject = jsonArray[i] as JSONObject
            var latitude = jsonObj.optDouble(latitude)
            var longitude = jsonObj.optDouble(longitude)
            var accuracy = BigDecimal.valueOf(jsonObj.getDouble(accuracy)).toFloat()
            var time = jsonObj.optLong(time)

            var location = Location(latitude, longitude, accuracy, time)
            locations.add(location)
            i++
        }
        return locations
    }
}
