package com.example.clipboardmanager.view.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.clipboardmanager.service.background.ClipBoardService
import android.app.ActivityManager
import com.example.clipboardmanager.R
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.clipboardmanager.service.model.ClipboardEntity
import android.os.Build
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class MainActivity : AppCompatActivity(){

    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ClipBoardFragment.newInstance())
                .commitNow()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 22)
        } else {
            initializeService()
        }

        /*if(!isMyServiceRunning(ClipBoardService::class.java)){
            InsertionUtils().scheduleChargingReminder(applicationContext)
            Log.d("started", "service started")
        }*/
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context))
    }

    fun show(clipboardEntity: ClipboardEntity) {
        val editClip = EditClipBoardFragment()
        val bundle = Bundle()
        clipboardEntity.id?.let { bundle.putInt("pass", it) }
        editClip.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("project")
            .replace(
                R.id.container,
                editClip, null
            ).commit()
       supportActionBar?.setDisplayHomeAsUpEnabled(true);// adds back button
        Log.d("#####",clipboardEntity.note +"\n" + clipboardEntity.date +"\n"+ clipboardEntity.number )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 22) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                startService(serviceIntent)
            } else { //Permission is not available
                Toast.makeText(this, "Draw over other app permission not available.", LENGTH_SHORT).show()
                initializeService()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private fun initializeService() {
        serviceIntent = Intent(this, ClipBoardService::class.java )
        startService(serviceIntent)
        if(!isMyServiceRunning(ClipBoardService::class.java)){
            startService(serviceIntent)
            Log.d("started", "service started")
        }
        else {
            Log.d("started", "service already running")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.settings_menu -> {
              startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
