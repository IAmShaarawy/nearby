package dev.elshaarawy.nearby.features.splash

import dev.elshaarawy.nearby.R
import dev.elshaarawy.nearby.base.BaseFragment
import dev.elshaarawy.nearby.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {
    override val viewModel: SplashViewModel by viewModel()

    override fun SplashViewModel.observeViewModel() = Unit
}