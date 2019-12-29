package dev.elshaarawy.nearby.features.home

import android.Manifest
import android.content.Intent
import dev.elshaarawy.nearby.R
import dev.elshaarawy.nearby.base.BaseFragment
import dev.elshaarawy.nearby.databinding.FragmentHomeBinding
import dev.elshaarawy.nearby.delegates.GPSSettingsDelegate
import dev.elshaarawy.nearby.delegates.PermissionsDelegate
import dev.elshaarawy.nearby.extensions.shot
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    private val locationPermission by PermissionsDelegate(
        LOCATION_PERMISSION_REQ_CODE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val gpsSettings by GPSSettingsDelegate()

    override val viewModel: HomeViewModel by viewModel()

    override fun HomeViewModel.observeViewModel() {
        runWithLocationPermissionAndGPS.shot(viewLifecycleOwner) {
            locationPermission.withPermission {
                gpsSettings.withGPSEnabled(false) {
                    onPermissionGrantedAndGPSEnabled()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        gpsSettings.onActivityResult(requestCode, resultCode, data) {
            // on gps off
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermission.onRequestPermissionsResult(requestCode, permissions, grantResults) {
            // on permission denied
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQ_CODE = 45
    }
}