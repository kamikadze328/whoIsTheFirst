package com.kamikadze328.whoisthefirst.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context

import com.google.android.gms.nearby.Nearby

import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.Strategy
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.lang.Exception
import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo

import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback





class OnlineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    private fun startAdvertising() {

        val endpointDiscoveryCallback: EndpointDiscoveryCallback =
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                    // An endpoint was found. We request a connection to it.
                    Nearby.getConnectionsClient(context)
                        .requestConnection(
                            getLocalUserName(),
                            endpointId,
                            connectionLifecycleCallback
                        )
                        .addOnSuccessListener(
                            OnSuccessListener { unused: Void? -> })
                        .addOnFailureListener(
                            OnFailureListener { e: Exception? -> })
                }

                override fun onEndpointLost(endpointId: String) {
                    // A previously discovered endpoint has gone away.
                }
            }


}