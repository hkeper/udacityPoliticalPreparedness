package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class LocationPermissionsUtil(private var listener: PermissionListener?) {

    interface PermissionListener {
        fun onGranted()
        fun onDenied()
    }

    companion object {
        private const val PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    fun requestPermissions(fragment: Fragment) {
        if (isPermissionGranted(fragment.requireContext())) {
            listener?.onGranted()
        } else {
            requestPermission(fragment)
        }
    }

    fun unregister() {
        listener = null
    }

    private fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(fragment: Fragment) {
        val permissions = ActivityResultContracts.RequestPermission()
        fragment.registerForActivityResult(permissions) {
            if (it) {
                listener?.onGranted()
            } else {
                listener?.onDenied()
            }
        }.launch(PERMISSION, ActivityOptionsCompat.makeBasic())
    }
}