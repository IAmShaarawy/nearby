package dev.elshaarawy.nearby.data.livedata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dev.elshaarawy.nearby.extensions.isLocationEnabled

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class IsGPSEnabledLiveData(private val ctx: Context) : LiveData<Boolean>() {

    init {
        checkGPSStatusAndPublishValue()
    }

    private val gpsChangeReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                checkGPSStatusAndPublishValue()
            }
        }
    }

    override fun getValue(): Boolean {
        checkGPSStatusAndPublishValue()
        return super.getValue()!!
    }

    override fun onActive() {
        super.onActive()
        checkGPSStatusAndPublishValue()
        ctx.registerReceiver(gpsChangeReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

    override fun onInactive() {
        super.onInactive()
        ctx.unregisterReceiver(gpsChangeReceiver)
    }

    override fun removeObserver(observer: Observer<in Boolean>) {
        value = false
        super.removeObserver(observer)
    }

    private fun checkGPSStatusAndPublishValue() {
        val isLocationEnabled = ctx.isLocationEnabled()

        if (isLocationEnabled == super.getValue()) // to make it distinct
            return
        this.value = isLocationEnabled
    }
}