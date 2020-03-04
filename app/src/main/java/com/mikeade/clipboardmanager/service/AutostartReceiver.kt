package com.mikeade.clipboardmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.mikeade.clipboardmanager.service.background.ClipBoardService

class AutostartReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {//
        Log.d("Autostart", "Broadcast received = ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.QUICKBOOT_POWERON") {

            Log.i("Autostart", "started")
            //Toast.makeText(context, "BOOT RECEIVED", LENGTH_SHORT).show()
            val serviceIntent = Intent(context, ClipBoardService::class.java)
            context.startService(serviceIntent)
            //context.startForegroundService(serviceIntent)
            //val serviceIntent = Intent(context, ClipJobIntentService::class.java);

            /*if (context != null) {
                ClipJobIntentService().enqueueWork(context, serviceIntent)
            }*/
        }
    }
}