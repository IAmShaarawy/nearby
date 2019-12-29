package dev.elshaarawy.nearby.data.repositories

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import dev.elshaarawy.nearby.data.entities.Coordinates
import dev.elshaarawy.nearby.data.entities.ExploreResponse
import dev.elshaarawy.nearby.data.livedata.LocationUpdatesLiveData
import dev.elshaarawy.nearby.data.network.services.VenueService
import kotlinx.coroutines.*

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
interface VenueRepository {

    suspend fun getSingleVenueUpdate(): ExploreResponse
    fun getVenueUpdates(coroutineScope: CoroutineScope): LiveData<ExploreResponse>


    companion object : (Context, VenueService) -> VenueRepository {
        override fun invoke(ctx: Context, service: VenueService): VenueRepository =
            VenueRepositoryImpl(ctx, service)
    }
}

private class VenueRepositoryImpl(
    private val ctx: Context,
    private val service: VenueService
) : VenueRepository {

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    override suspend fun getSingleVenueUpdate(): ExploreResponse = withContext(Dispatchers.IO) {
        val freshLocation = LocationUpdatesLiveData.freshLocation(ctx)
        service.explore(freshLocation.requestQuarryParameters)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    override fun getVenueUpdates(coroutineScope: CoroutineScope): LiveData<ExploreResponse> {
        return MediatorLiveData<ExploreResponse>().apply {
            addSource(LocationUpdatesLiveData(ctx)) {
                if (shouldUpdate(it, value?.coordinates))
                    coroutineScope.launch(Dispatchers.IO) {
                        service.explore(it.requestQuarryParameters)
                            .apply { coordinates = it }
                            .also { postValue(it) }
                    }
            }
        }
    }

    private fun shouldUpdate(start: Coordinates, end: Coordinates?): Boolean {
        if (end == null)
            return true

        return calculateDistance(start, end) >= 500
    }

    private fun calculateDistance(start: Coordinates, end: Coordinates): Int {
        val provider = "LocationUpdatesLiveData"
        val startLocation = start.toLocation(provider)
        val endLocation = end.toLocation(provider)
        return startLocation.distanceTo(endLocation).toInt()
    }

}