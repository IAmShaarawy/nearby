package dev.elshaarawy.nearby.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dev.elshaarawy.nearby.BR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel>(@LayoutRes contentLayoutId: Int) :
    Fragment(contentLayoutId), CoroutineScope by MainScope() {

    private lateinit var privateBinding: B

    protected val binding: B
        get() = if (isViewCreated) privateBinding
        else throw IllegalStateException("binding should be not null at this state.")

    private var isViewCreated = false

    protected abstract val viewModel: VM

    protected abstract fun VM.observeViewModel()

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isViewCreated = true
        return if (::privateBinding.isInitialized) privateBinding.root
        else run {
            super.onCreateView(inflater, container, savedInstanceState)!!.also {
                privateBinding = DataBindingUtil.bind(it)!!
            }
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, viewModel)
        }
        viewModel.observeViewModel()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}