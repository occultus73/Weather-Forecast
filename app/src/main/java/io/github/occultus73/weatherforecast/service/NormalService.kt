package io.github.occultus73.weatherforecast.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class NormalService: Service() {
    val TAG = "TAG_X"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")

        for(i in 0 until 5) {
            Log.d("TAG_X", "Self destruct in ${(5-i)}")
            Thread.sleep(1000)
        }
        stopSelf()
        return super.onStartCommand(intent, flags, startId)

    }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}