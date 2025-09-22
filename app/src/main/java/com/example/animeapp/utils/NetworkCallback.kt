package com.example.animeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class NetworkCallback(
    private val context: Context,
    private val onStatusChanged: (Boolean) -> Unit
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        onStatusChanged(true) // Online
    }

    override fun onLost(network: Network) {
        onStatusChanged(false) // Offline
    }
}
