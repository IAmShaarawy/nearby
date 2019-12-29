package dev.elshaarawy.nearby.features.home

import dev.elshaarawy.nearby.R
import dev.elshaarawy.nearby.base.BaseFragment
import dev.elshaarawy.nearby.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    override val viewModel: HomeViewModel by viewModel()

    override fun HomeViewModel.observeViewModel() = Unit
}