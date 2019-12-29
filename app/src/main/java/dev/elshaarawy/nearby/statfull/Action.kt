package com.forsale.app.base

import android.view.View
import androidx.annotation.IdRes

/**Runtime annotation that targets functions to operate on these functions with [ActionScanner] subclasses.
 * @param viewId array of android [View] id that it's click listener invoke method annotated with [Action]
 * @author Mohamed Elshaarawy on Dec 15, 2018.
 */
@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class Action(@IdRes vararg val viewId: Int)