package dev.elshaarawy.nearby.features.home

import androidx.lifecycle.*
import dev.elshaarawy.nearby.data.entities.ExploreResponse
import dev.elshaarawy.nearby.data.entities.ExploreResponse.Group.Item
import dev.elshaarawy.nearby.data.repositories.PreferencesRepository
import dev.elshaarawy.nearby.data.repositories.VenueRepository
import dev.elshaarawy.nearby.extensions.launch

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class HomeViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val venueRepository: VenueRepository
) : ViewModel() {
    private val _runWithLocationPermissionAndGPS = MutableLiveData<Unit>()
    val runWithLocationPermissionAndGPS: LiveData<Unit> = _runWithLocationPermissionAndGPS

    private val _exploreResponse = MediatorLiveData<ExploreResponse>()

    val items: LiveData<List<Item>> =
        Transformations.map(_exploreResponse) { it.groups.flatMap { it.items } }

    private val locationBroadcastSource by lazy { venueRepository.getVenueUpdates(viewModelScope) }

    init {
        _runWithLocationPermissionAndGPS.postValue(Unit)
    }

    fun onPermissionGrantedAndGPSEnabled() {
        if (preferencesRepository.isSingleUpdate) {
            loadSingleUpdate()
        } else {
            listenForLocationBroadcast()
        }
    }

    private fun loadSingleUpdate() {
        launch {
            venueRepository.getSingleVenueUpdate()
                .also(_exploreResponse::postValue)
        }
    }

    private fun listenForLocationBroadcast() {
        _exploreResponse.apply {
            removeSource(locationBroadcastSource)
            addSource(locationBroadcastSource, this::postValue)
        }

    }
}