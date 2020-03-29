package com.github.gibbrich.airmee.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.gibbrich.airmee.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun RecyclerView.redraw() {
    this.adapter = adapter
}

fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun getErrorView(
    root: View,
    @BaseTransientBottomBar.Duration duration: Int,
    @StringRes message: Int,
    @StringRes actionMessage: Int,
    action: () -> Unit
) = Snackbar
    .make(root, message, duration)
    .decorate(R.color.snack_background_color)
    .also { snackbar ->
        snackbar.setAction(actionMessage) {
            action.invoke()
            snackbar.dismiss()
        }
    }

private fun Snackbar.decorate(
    @ColorRes backgroundId: Int,
    @ColorRes textColorId: Int = android.R.color.white,
    @ColorRes actionColor: Int = android.R.color.white
): Snackbar {
    val layout: Snackbar.SnackbarLayout = view as Snackbar.SnackbarLayout
    layout.setBackgroundColor(context.getColorCompat(backgroundId))

    val textView = layout.findViewById<TextView>(R.id.snackbar_text)

    textView.setTextColor(context.getColorCompat(textColorId))

    textView.typeface = android.graphics.Typeface.SANS_SERIF
    textView.maxLines = Int.MAX_VALUE
    textView.ellipsize = null

    val action = layout.findViewById<TextView>(R.id.snackbar_action)
    action.setTextColor(context.getColorCompat(actionColor))
    action.typeface = android.graphics.Typeface.SANS_SERIF
    return this
}