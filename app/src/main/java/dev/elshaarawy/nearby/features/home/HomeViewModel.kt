package dev.elshaarawy.nearby.features.home

import androidx.lifecycle.*
import dev.elshaarawy.nearby.data.entities.ExploreResponse
import dev.elshaarawy.nearby.data.entities.ExploreResponse.Group.Item
import dev.elshaarawy.nearby.data.repositories.PreferencesRepository
import dev.elshaarawy.nearby.data.repositories.VenueRepository
import dev.elshaarawy.nearby.extensions.launch
import dev.elshaarawy.nearby.statfull.ViewStates
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

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

    private val setStateTo = MutableLiveData<ViewStates>()

    val contentState: LiveData<Unit> =
        Transformations.map(items) { if (it.isNotEmpty()) Unit else null }
    val loadingState: LiveData<Unit> =
        Transformations.map(setStateTo) { if (it == ViewStates.LOADING) Unit else null }
    val emptyState: LiveData<Unit> =
        Transformations.map(items) {
            if (it.isEmpty()) Unit else null
        }
    val errorState: LiveData<Unit> =
        Transformations.map(setStateTo) { if (it == ViewStates.ERROR) Unit else null }

    private val locationBroadcastSource by lazy { venueRepository.getVenueUpdates(viewModelScope) }

    init {
        _runWithLocationPermissionAndGPS.postValue(Unit)
        viewModelScope.coroutineContext + CoroutineExceptionHandler { _, thr ->
            Timber.e(thr)
            setStateTo.postValue(ViewStates.ERROR)
        }
    }

    fun onPermissionGrantedAndGPSEnabled() {
        setStateTo.postValue(ViewStates.LOADING)
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