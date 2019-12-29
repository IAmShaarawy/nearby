package dev.elshaarawy.nearby.features.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.forsale.app.base.ActionScanner
import dev.elshaarawy.nearby.R
import dev.elshaarawy.nearby.base.BaseFragment
import dev.elshaarawy.nearby.databinding.FragmentHomeBinding
import dev.elshaarawy.nearby.delegates.GPSSettingsDelegate
import dev.elshaarawy.nearby.delegates.PermissionsDelegate
import dev.elshaarawy.nearby.extensions.shot
import dev.elshaarawy.nearby.features.home.item.NearbyAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home),
    ActionScanner {

    private val locationPermission by PermissionsDelegate(
        LOCATION_PERMISSION_REQ_CODE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val gpsSettings by GPSSettingsDelegate()

    private val nearyAdapter by lazy { NearbyAdapter() }
    override val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = nearyAdapter
        }
    }

    override fun HomeViewModel.observeViewModel() {
        contentState.shot(viewLifecycleOwner) {
            binding.stateContainer.restToContent()
        }

        loadingState.shot(viewLifecycleOwner) {
            binding.stateContainer.changeStateAndBindActions(R.layout.loading, this@HomeFragment)
        }

        errorState.shot(viewLifecycleOwner) {
            binding.stateContainer.changeStateAndBindActions(R.layout.error, this@HomeFragment)
        }

        emptyState.shot(viewLifecycleOwner) {
            binding.stateContainer.changeStateAndBindActions(R.layout.empty, this@HomeFragment)
        }

        runWithLocationPermissionAndGPS.shot(viewLifecycleOwner) {
            locationPermission.withPermission {
                gpsSettings.withGPSEnabled(false) {
                    onPermissionGrantedAndGPSEnabled()
                }
            }
        }

        items.observe(viewLifecycleOwner, Observer {
            nearyAdapter.submitList(it)
        })
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