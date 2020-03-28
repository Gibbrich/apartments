package com.github.gibbrich.airmee.core.manager

import android.content.Context
import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes stringRes: Int): String
}

class ResourceDataManager(private val context: Context) :
    ResourceManager {

    override fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }
}