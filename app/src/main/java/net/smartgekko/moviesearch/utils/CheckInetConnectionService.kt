package net.smartgekko.moviesearch.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.net.URL
import java.net.URLConnection

class CheckInetConnectionService : Service() {
    private var checkIt: Boolean = false

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var connectivity: Boolean
        var connectionState = true

        Thread {
            while (true) {
                checkIt = MyApplication.isCheckConnection()
                Thread.sleep(CHECK_INET_PERIOD)

                if (checkIt) {
                    try {
                        val url = URL(TMDB_BASE_HOST)
                        val conn: URLConnection = url.openConnection()
                        conn.setConnectTimeout(CHECK_INET_TIMEOUT)
                        conn.connect()
                        connectivity = true
                    } catch (e: Exception) {
                        connectivity = false
                    }

                    if (connectivity) {
                        if (!connectionState) {
                            sendConnectionBroadcast(connectivity)
                            connectionState = true
                            checkIt = false
                        } else {
                            checkIt = false
                        }
                    } else {
                        if (connectionState) {
                            sendConnectionBroadcast(connectivity)
                            connectionState = false
                            checkIt = true
                        }
                    }
                }
            }
        }.start()

        return START_STICKY
    }

    private fun sendConnectionBroadcast(isConnected: Boolean) {
        val broadcastIntent = Intent()
        broadcastIntent.putExtra("connected", isConnected)
        broadcastIntent.action = INTENT_ACTION_KEY
        sendBroadcast(broadcastIntent)
    }
}
