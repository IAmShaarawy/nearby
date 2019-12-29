package dev.elshaarawy.nearby.extensions

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, block)

fun <T> LiveData<T>.shot(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    if (this is MutableLiveData)
        this.observe(lifecycleOwner, Observer {
            it?.also(observer)
                .also { postValue(null) }
        })
    else
        throw IllegalArgumentException("Bullet: this context is not MutableLiveData")
}