package com.github.gibbrich.airmee.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun isLocationPermissionGranted(context: Context): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION) ||
            isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

fun getLocationPermissions(): Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

private fun isPermissionGranted(context: Context, permission: String): Boolean =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
