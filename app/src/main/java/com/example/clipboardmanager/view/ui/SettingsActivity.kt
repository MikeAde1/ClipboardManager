package com.example.clipboardmanager.view.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.clipboardmanager.R

class Settings : AppCompatActivity() {

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
