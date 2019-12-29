package dev.elshaarawy.nearby.injection

import dev.elshaarawy.nearby.app.AppViewModel
import dev.elshaarawy.nearby.features.home.HomeViewModel
import dev.elshaarawy.nearby.features.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
object ViewModelsModule : () -> Module {
    override fun invoke(): Module = module {
        viewModel { AppViewModel(get()) }
        viewModel { SplashViewModel() }
        viewModel { HomeViewModel(get(), get()) }
    }

}