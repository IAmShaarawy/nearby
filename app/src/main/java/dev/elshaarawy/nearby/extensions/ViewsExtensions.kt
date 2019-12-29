package dev.elshaarawy.nearby.extensions

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * @author Mohamed Elshaarawy on Dec 24, 2019.
 */
@BindingAdapter(value = ["isGone"])
fun View.isGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}