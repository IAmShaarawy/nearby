package com.forsale.app.base

import android.view.View

/**
 * This interface have only one default method that scan subclasses methods annotated with [Action] annotation
 * @author Mohamed Elshaarawy on Dec 16, 2018.
 */
 interface ActionScanner {

    /** This function scans the class methods annotated with [Action] annotation
     * to add click listener to views id in this [Action] annotation
     * @author Mohamed Elshaarawy*/
    fun setStateViewsActions(rootView: View) {
        this.javaClass
            .declaredMethods
            .filter { it.isAnnotationPresent(Action::class.java) }
            .flatMap {
                it.getAnnotation(Action::class.java)
                    .viewId
                    .toSet()
                    .map { id -> it to id }
            }
            .forEach { (method, viewId) ->
                rootView.findViewById<View>(viewId)?.setOnClickListener { view ->
                    try {
                        method.invoke(this, view)
                    } catch (e: IllegalArgumentException) {
                        throw IllegalArgumentException("Methods annotated with ${Action::class.java} should have only one argument of type ${View::class.java}")
                    }
                }
            }
    }
}