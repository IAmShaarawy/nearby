package dev.elshaarawy.nearby.data.network.services

import dev.elshaarawy.nearby.data.entities.ExploreResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
interface VenueService {
    @GET("venues/explore")
    suspend fun explore(@Query("ll") latitudeLongitude: String): ExploreResponse
}