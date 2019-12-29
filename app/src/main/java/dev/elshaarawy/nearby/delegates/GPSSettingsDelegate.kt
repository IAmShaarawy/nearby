package dev.elshaarawy.nearby.delegates

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import dev.elshaarawy.nearby.data.livedata.IsGPSEnabledLiveData
import kotlin.reflect.KProperty

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class GPSSettingsDelegate {

    private lateinit var instance: GPSSettingsManager

    operator fun getValue(thisRef: Any, property: KProperty<*>): GPSSettingsManager {
        return if (::instance.isInitialized) instance else
            when (thisRef) {
                is Fragment -> FragmentGPSSettingsManager(
                    thisRef
                )
                is AppCompatActivity -> AppCompatActivityGPSSettingsManager(
                    thisRef
                )
                else -> throw TypeCastException("Can't cast $thisRef to any thing because it is not supported")
            }.also { instance = it }
    }

    abstract class GPSSettingsManager(private val ctx: Context) {
        protected abstract val lifecycleOwner: LifecycleOwner
        private val isGPSEnabledLiveData =
            IsGPSEnabledLiveData(ctx)
        fun withGPSEnabled(
            onlyOneObservation: Boolean = true,
            onEnableGPS: ((() -> Unit) -> Unit)? = null,
            onEnabled: () -> Unit
        ) {
            val gpsObserver = Observer<Boolean> {
                if (it) {
                    onEnabled()
                    if (onlyOneObservation) {
                        isGPSEnabledLiveData.removeObservers(lifecycleOwner)
                    }
                } else {
                    onEnableGPS?.invoke {
                        enableGPS()
                    } ?: enableGPS()
                }
            }
            isGPSEnabledLiveData.observe(lifecycleOwner, gpsObserver)
        }

        protected abstract fun enableGPS()

        protected fun validateGPS(onNotEnabled: (ResolvableApiException) -> Unit) {
            val builder = LocationSettingsRequest.Builder()
            LocationRequest.create()
                .let(builder::addLocationRequest)
            LocationServices.getSettingsClient(ctx)
                .checkLocationSettings(builder.build())!!
                .addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult(). // here we don't use it because we depend on IsGPSEnabledLiveData
                            onNotEnabled(exception)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }
                }
        }

        fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            onReject: () -> Unit = {}
        ) {
            if (requestCode == REQ_CODE && resultCode != Activity.RESULT_OK)
                onReject()
        }
    }

    private class FragmentGPSSettingsManager(private val fragment: Fragment) :
        GPSSettingsManager(fragment.context!!) {
        override val lifecycleOwner: LifecycleOwner = fragment.viewLifecycleOwner

        override fun enableGPS() {
            validateGPS {
                fragment.startIntentSenderForResult(
                    it.resolution.intentSender,
                    REQ_CODE, null, 0, 0, 0, null
                )
            }
        }
    }

    private class AppCompatActivityGPSSettingsManager(private val activity: AppCompatActivity) :
        GPSSettingsManager(activity) {
        override val lifecycleOwner: LifecycleOwner = activity

        override fun enableGPS() {
            validateGPS {
                it.startResolutionForResult(activity,
                    REQ_CODE
                )
            }
        }
    }

    companion object {
        private const val REQ_CODE = 99
    }
}