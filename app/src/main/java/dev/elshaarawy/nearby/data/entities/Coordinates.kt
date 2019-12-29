package dev.elshaarawy.nearby.data.entities

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
data class Coordinates(val latitude: Double, val longitude: Double) {
    val requestQuaryParamter: String
        get() = "$latitude,$longitude"
}