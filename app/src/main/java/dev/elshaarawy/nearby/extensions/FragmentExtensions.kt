package dev.elshaarawy.nearby.extensions

import androidx.fragment.app.Fragment
import dev.elshaarawy.nearby.app.AppViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
val Fragment.appViewModel: AppViewModel
    get() = sharedViewModel<AppViewModel>().value