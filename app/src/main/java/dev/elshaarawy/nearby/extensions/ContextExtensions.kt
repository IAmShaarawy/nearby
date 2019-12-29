package dev.elshaarawy.nearby.extensions

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
fun Context.isConnected() =
    getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isConnected ?: false

fun Context.isLocationEnabled() =
    getSystemService<LocationManager>()?.run {
        isProviderEnabled(LocationManager.GPS_PROVIDER) || isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } ?: false