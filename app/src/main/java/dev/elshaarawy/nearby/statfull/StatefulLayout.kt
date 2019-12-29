package dev.elshaarawy.nearby.statfull

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.contains
import com.forsale.app.base.ActionScanner

class StatefulLayout
@JvmOverloads
constructor(
    ctx: Context,
    attSet: AttributeSet? = null,
    defStyle: Int = 0
) :
    FrameLayout(ctx, attSet, defStyle) {
    private val viewsMap = mutableMapOf<Int, View>()

    init {
        if (childCount > 1) {
            throw IllegalStateException("You must have only one child in ${StatefulLayout::class.qualifiedName}")
        }

        layoutTransition =
            LayoutTransition() // applying a layout transition with default animations.
    }

    fun changeStateTo(@LayoutRes layoutId: Int) {
        viewsMap[layoutId]?.also {
            // check if the state is already visible
            it.takeUnless(this::contains)
                ?.also(this::addToTemplate)
        } ?: apply {
            View.inflate(context, layoutId, null)
                .also {
                    viewsMap[layoutId] = it
                    addToTemplate(it)
                }
        }
    }

    /** Reset [StatefulLayout] to it's main content*/
    fun restToContent() {
        viewsMap.values
            .filter(this::contains)
            .forEach(this::removeView)
//        this.forEach { it.visibility = View.VISIBLE } // show main content
    }

    fun changeStateAndBindActions(@LayoutRes targetLayout: Int, actionScanner: ActionScanner) {
        changeStateTo(targetLayout)
        actionScanner.setStateViewsActions(this)
    }

    private fun addToTemplate(view: View) {
        viewsMap.values
            .filterNot { it == view }
            .filter(this::contains)
            .forEach(this::removeView)

        view.takeUnless(this::contains)
            .apply(this::addView)
    }
}