package com.example.clipboardmanager.service.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class PowerConnectionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
       /* if (intent?.action.equals(Intent.)){
            Log.i("Autostart", "started")
            Toast.makeText(context, "started", Toast.LENGTH_SHORT).show()
        }*/

        if ((intent?.getAction().equals("android.intent.action.BOOT_COMPLETED")
                    || intent?.getAction().equals("android.intent.action.QUICKBOOT_POWERON")
                    || intent?.getAction().equals("com.htc.intent.action.QUICKBOOT_POWERON"))){
            Toast.makeText(context, "FIRED BOOT COMPLETE" , Toast.LENGTH_LONG).show();
        }
}}