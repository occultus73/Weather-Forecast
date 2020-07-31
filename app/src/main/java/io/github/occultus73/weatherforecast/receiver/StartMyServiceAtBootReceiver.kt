package io.github.occultus73.weatherforecast.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.occultus73.weatherforecast.service.NormalService

class StartMyServiceAtBootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent?.action) {
            val serviceIntent = Intent(context, NormalService::class.java)
            context?.startService(serviceIntent)
        }
    }
}