package dev.elshaarawy.nearby.data.entities

import android.location.Location

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
data class Coordinates(val latitude: Double, val longitude: Double) {
    val requestQuarryParameters: String
        get() = "$latitude,$longitude"
    fun toLocation(provider:String) = Location(provider).apply {
        latitude  = this@Coordinates.latitude
        longitude  = this@Coordinates.longitude
    }
}