package com.github.gibbrich.airmee.ui.utils

import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class SnapHelperOneByOne : LinearSnapHelper() {
    companion object {
        private const val VELOCITY_THRESHOLD = 400
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return RecyclerView.NO_POSITION
        }

        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION

        layoutManager as LinearLayoutManager

        return when {
            velocityX > VELOCITY_THRESHOLD -> layoutManager.findLastVisibleItemPosition()
            velocityX < VELOCITY_THRESHOLD -> layoutManager.findFirstVisibleItemPosition()
            else -> layoutManager.getPosition(currentView)
        }
    }
}