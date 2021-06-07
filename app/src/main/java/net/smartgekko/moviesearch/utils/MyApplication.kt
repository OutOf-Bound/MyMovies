package net.smartgekko.moviesearch.utils

import android.content.Context
import androidx.room.Room
import net.smartgekko.moviesearch.model.database.database.MainDatabase


class MyApplication(contextArg: Context) {
    var context: Context = contextArg

    fun create() {
        setAppContext(context, this)
    }

    companion object {

        private lateinit var contextIn: Context
        private var database: MainDatabase? = null
        private var instance: MyApplication? = null
        private var inetConnected: Boolean = true
        private var checkConnection: Boolean = false

        fun setAppContext(context: Context, inst: MyApplication) {
            contextIn = context
            instance = inst
            database = Room.databaseBuilder(context, MainDatabase::class.java, "database")
                .allowMainThreadQueries()
                .build()
        }

        fun getAppContext(): Context = contextIn
        fun getDatabase(): MainDatabase? = database
        fun setInetConnected(connected: Boolean) {
            inetConnected = connected
        }

        fun isInetConnected(): Boolean = inetConnected
        fun setCheckConnection(check: Boolean) {
            checkConnection = check
        }

        fun isCheckConnection(): Boolean = checkConnection

    }
}