package com.mikeade.clipboardmanager.view.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.mikeade.clipboardmanager.R

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.pref_general)

        val sharedPreferences = preferenceScreen.sharedPreferences

        val prefScreen = preferenceScreen.getPreference(0) //preference of ListPreference
        val value = sharedPreferences.getString(prefScreen.key, "2")
        setPreferenceSummary(prefScreen, value!!)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, preferenceKey: String) {

        val preference = findPreference(preferenceKey)
        if (null != preference){
            if (preference is android.support.v7.preference.ListPreference) {
                val value = sharedPreferences?.getString(preference.getKey(), "2" )//20,1,2 -- entryvalues
                setPreferenceSummary(preference, value!!)
                //Toast.makeText(context, value, LENGTH_SHORT).show()
            }
        }
    }

    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is android.support.v7.preference.ListPreference){
            val prefIndex = preference.findIndexOfValue(value)
            if (prefIndex >= 0){
                preference.setSummary(preference.entries[prefIndex])// gets the entries and not entryvalues
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}