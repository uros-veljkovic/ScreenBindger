package com.example.screenbindger.util.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


fun Context?.isOnline(): Boolean {
    val connectivityManager =
        this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.run {
            return hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } ?: return false
}