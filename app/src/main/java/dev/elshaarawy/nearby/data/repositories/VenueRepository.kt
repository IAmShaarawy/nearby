package dev.elshaarawy.nearby.data.repositories

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import dev.elshaarawy.nearby.data.entities.ExploreResponse
import dev.elshaarawy.nearby.data.livedata.LocationUpdatesLiveData
import dev.elshaarawy.nearby.data.network.services.VenueService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
interface VenueRepository {

    suspend fun getVenueUpdates(): Flow<ExploreResponse>


    companion object : (Context, VenueService) -> VenueRepository {
        override fun invoke(ctx: Context, service: VenueService): VenueRepository =
            VenueRepositoryImpl(ctx, service)
    }
}

private class VenueRepositoryImpl(private val ctx: Context, private val service: VenueService) :
    VenueRepository {

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    override suspend fun getVenueUpdates() = withContext(Dispatchers.IO) {
        flow {
            LocationUpdatesLiveData(ctx).observeForever {
                this@withContext.launch {
                    this@flow.emit(service.explore(it.requestQuaryParamter))
                }
            }
        }
    }

}