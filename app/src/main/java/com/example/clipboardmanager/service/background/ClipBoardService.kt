package com.example.clipboardmanager.service.background

import android.app.NotificationManager
import android.app.Service
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.service.repository.ClipBoardRepository
import java.lang.Exception

class ClipBoardService : Service() {

    lateinit var clipboard : ClipboardManager

    private lateinit var clipBoardRepository: ClipBoardRepository

    private var mPreviousText=""

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ClipboardService", "Service started")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "clipboardNotification")
                .setContentTitle("Clipboard service running")
                .setContentText("")
                .build()

        try {
            startForeground(90, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        }

        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardRepository = ClipBoardRepository(application)
        clipboard.addPrimaryClipChangedListener(primaryClipChangedListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private val primaryClipChangedListener = object: ClipboardManager.OnPrimaryClipChangedListener{
        override fun onPrimaryClipChanged() {
            var pasteData: String = ""
            if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription()!!.hasMimeType(MIMETYPE_TEXT_PLAIN)){
                val item = clipboard.primaryClip?.getItemAt(0)?.text
                pasteData = item.toString()
                if(mPreviousText.equals(pasteData)) return
                else {
                    checkForDuplicate(pasteData)
                }
            }
        }
    }

    private fun checkForDuplicate(pasteData: String) {
        val list: List<ClipboardEntity> = clipBoardRepository.loadNotes()
        val size = list.size
        Log.d("######", size.toString())

        var copies = 1
        for (note in 0..size-1){
            if (list.get(note).note.equals(pasteData)) {
                copies = copies + 1
            }
        }
        val clipboardEntity = ClipboardEntity(note = pasteData, time = System.currentTimeMillis().toString(), number = copies)
        clipBoardRepository.insert(clipboardEntity)
        mPreviousText = pasteData
        Toast.makeText(applicationContext, "New note copied: $pasteData \n Note has been copied $copies times" , LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        Log.w("ClipboardService", "Service is closing")
        super.onDestroy()
    }
}



