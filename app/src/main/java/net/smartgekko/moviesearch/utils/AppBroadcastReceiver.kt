package net.smartgekko.moviesearch.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.widget.Toast
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.model.repositories.MoviesRepository.updateConnectionInfo
import net.smartgekko.moviesearch.utils.MyApplication.Companion.setCheckConnection
import net.smartgekko.moviesearch.utils.MyApplication.Companion.setInetConnected


class AppBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var text = ""
        when (intent.action) {
            INTENT_ACTION_KEY -> {
                if (intent.getBooleanExtra("connected", true)) {
                    text = context.resources.getString(R.string.connection_ok)
                    setInetConnected(true)
                    setCheckConnection(false)
                    updateConnectionInfo(true)
                } else {
                    text = context.resources.getString(R.string.no_connection)
                    setInetConnected(false)
                    updateConnectionInfo(false)
                }
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
            CONNECTIVITY_ACTION -> {
                setCheckConnection(true)
            }
        }
    }
}
