package com.example.clipboardmanager.view.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.clipboardmanager.service.background.ClipBoardService
import android.app.ActivityManager
import com.example.clipboardmanager.R
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import com.example.clipboardmanager.service.AutostartReceiver
import com.example.clipboardmanager.service.model.ClipboardEntity


class MainActivity : AppCompatActivity() {

    private var me: Boolean= false

    private lateinit var serviceIntent: Intent

    lateinit var startReceiver: AutostartReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ClipBoardFragment.newInstance())
                .commitNow()
        }


        serviceIntent = Intent(this, ClipBoardService::class.java )
        startReceiver = AutostartReceiver()

        /*if(!isMyServiceRunning(ClipBoardService::class.java)){
            InsertionUtils().scheduleChargingReminder(applicationContext)
            Log.d("started", "service started")
        }
        else {
            Log.d("started", "service already running")
        }*/
        // applicationContext.registerReceiver(startReceiver, IntentFilter("android.intent.action.BOOT_COMPLETED"))
    }



    override fun onResume() {
        super.onResume()
        if(!isMyServiceRunning(ClipBoardService::class.java)){
            startService(serviceIntent)
            Log.d("started", "service started")
        }
        else {
            Log.d("started", "service already running")
        }
    }

    fun show(id: Int, clipboardEntity: ClipboardEntity) {
        val editclip = EditClipBoardFragment()
        val bundle = Bundle()
        bundle.putInt("pass",id)
        editclip.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("project")
            .replace(
                R.id.container,
                editclip, null
            ).commit()
         me = true
       supportActionBar?.setDisplayHomeAsUpEnabled(true);// adds back button
        Log.d("#####",clipboardEntity.note +"\n" + clipboardEntity.time +"\n"+ clipboardEntity.number )
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        // removes back button
        //getSupportActionBa?.setDisplayHomeAsUpEnabled(false);
        return true
    }

    override fun onBackPressed() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onBackPressed()
    }

    /*override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(startReceiver)
    }*/

}
