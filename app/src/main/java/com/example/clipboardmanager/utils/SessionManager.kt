package com.example.clipboardmanager.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(ctx: Context) {
    private val sharedPreference: SharedPreferences = ctx.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    var edit: SharedPreferences.Editor

    init {
        edit = with(sharedPreference) { return@with edit() }  //sharedPreference.edit()
    }

    fun displayActive(key:String , value : Boolean ){
        edit.putBoolean(key,value).apply()
    }

    fun getNotes(): Boolean{
        return sharedPreference.getBoolean("active",true)
    }
}