package com.example.clipboardmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.clipboardmanager.service.background.ClipBoardService

class AutostartReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {//
        Log.d("Autostart", "Broadcast received = ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.QUICKBOOT_POWERON") {

            Log.i("Autostart", "started")
            Toast.makeText(context, "BOOT RECEIVED", LENGTH_SHORT).show()
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