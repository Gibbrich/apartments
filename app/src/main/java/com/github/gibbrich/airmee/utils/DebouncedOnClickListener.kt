package com.github.gibbrich.airmee.utils

import android.os.SystemClock
import android.view.View
import java.util.*

/**
 * Debounces clicks
 * @param minimumInterval The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
 */
class DebouncedOnClickListener(
    private val minimumInterval: Long,
    private val onDebouncedClick: (View) -> Unit
) : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long> = WeakHashMap()

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || currentTimestamp - previousClickTimestamp.toLong() > minimumInterval) {
            onDebouncedClick.invoke(clickedView)
        }
    }
}