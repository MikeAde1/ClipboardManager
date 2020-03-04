package com.mikeade.clipboardmanager.view.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mikeade.clipboardmanager.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commitNow()
        }
    }


}
