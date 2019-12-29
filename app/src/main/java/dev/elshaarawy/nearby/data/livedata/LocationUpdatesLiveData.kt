package dev.elshaarawy.nearby.data.livedata

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dev.elshaarawy.nearby.data.entities.Coordinates

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class LocationUpdatesLiveData(ctx: Context, private val singleUpdate: Boolean = false) :
    LiveData<Coordinates>() {

    private val fusedLocation by lazy { LocationServices.getFusedLocationProviderClient(ctx) }

    private val locationRequest by lazy {
        LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval =
                UPDATE_INTERVAL
            fastestInterval =
                FASTEST_INTERVAL
        }
    }

    private val locationCallbacks by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.apply {
                    postValue(Coordinates(latitude, longitude))
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocation.requestLocationUpdates(locationRequest, locationCallbacks, Looper.myLooper())
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocation.removeLocationUpdates(locationCallbacks)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun observe(owner: LifecycleOwner, observer: Observer<in Coordinates>) {
        removeObservers(owner)
        super.observe(owner, observer)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun observeForever(observer: Observer<in Coordinates>) {
        removeObserver(observer)
        super.observeForever(observer)
    }

    override fun postValue(value: Coordinates?) {
        super.postValue(value)
        if (singleUpdate)
            fusedLocation.removeLocationUpdates(locationCallbacks)
    }

    override fun setValue(value: Coordinates?) {
        super.setValue(value)
        if (singleUpdate)
            fusedLocation.removeLocationUpdates(locationCallbacks)
    }

    companion object {
        private const val UPDATE_INTERVAL = (2 * 60 * 1000).toLong() // two minutes
        private const val FASTEST_INTERVAL: Long = (15 * 1000).toLong() // 1/4 minute
    }
}