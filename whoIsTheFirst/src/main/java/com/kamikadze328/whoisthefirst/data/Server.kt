package com.kamikadze328.whoisthefirst.data

import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.Strategy

class Server {
    init {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
        Nearby.getConnectionsClient(this)
            .startAdvertising(
                getLocalUserName(), SERVICE_ID, connectionLifecycleCallback, advertisingOptions
            )
            .addOnSuccessListener(
                OnSuccessListener { unused: Void? -> })
            .addOnFailureListener(
                OnFailureListener { e: Exception? -> })
    }
}


