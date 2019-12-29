package dev.elshaarawy.nearby.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.elshaarawy.nearby.R
import dev.elshaarawy.nearby.data.repositories.PreferencesRepository

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class AppViewModel(preferencesRepository: PreferencesRepository) : ViewModel() {
    private val _isToolbarVisible = MutableLiveData<Boolean>()
    val isToolbarVisible: LiveData<Boolean> = _isToolbarVisible

    fun onDestinationChange(id: Int) {
        decideToolbarVisibility(id)
    }

    private fun decideToolbarVisibility(id: Int) {
        when (id) {
            R.id.splashFragment -> false
            else -> true
        }.also { _isToolbarVisible.postValue(it) }
    }
}