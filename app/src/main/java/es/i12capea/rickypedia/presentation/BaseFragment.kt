package es.i12capea.rickypedia.presentation

import android.content.Context
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*


abstract class BaseFragment : Fragment()
{
    private lateinit var connectivityManager : ConnectivityManager
    private var counter = 0
    private var cancelJobList = ArrayList<Job>()

    private val networkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            counter++
            if (counter == 1){
                onNetworkStatusChange(true)
            }
        }

        override fun onUnavailable() {
            counter = 0
            onNetworkStatusChange(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            disconnectedProccess()
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityManager = requireActivity()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        registerNetworkCallback()

    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterNetworkCallback()
    }

    abstract fun onNetworkStatusChange(available: Boolean)

    fun unRegisterNetworkCallback(){
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun disconnectedProccess() : Job{
        return viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                counter--
                if (counter <= 0){
                    onNetworkStatusChange(false)
                }
            }
        }
    }

    fun registerNetworkCallback() {
        try {
            val builder = NetworkRequest.Builder()

            connectivityManager.registerNetworkCallback(
                builder.build(),
                networkCallback
            )
        } catch (e: Exception) {
            onNetworkStatusChange(false)
        }
    }
}

