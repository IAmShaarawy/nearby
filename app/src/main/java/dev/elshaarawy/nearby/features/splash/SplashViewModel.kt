package dev.elshaarawy.nearby.features.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.elshaarawy.nearby.extensions.launch
import kotlinx.coroutines.delay

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class SplashViewModel : ViewModel() {
    private val _toNextScreen = MutableLiveData<Unit>()
    val toNextScreen: LiveData<Unit> = _toNextScreen

    init {
        launch {
            delay(3000)
            _toNextScreen.postValue(Unit)
        }
    }
}