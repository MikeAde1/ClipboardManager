package com.example.clipboardmanager.service.background

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.widget.Toast
import com.example.clipboardmanager.service.repository.ClipBoardRepository

class ClipJobIntentService: JobIntentService() {

    private lateinit var thread:Thread

    val JOB_ID = 0x01

    fun enqueueWork(context: Context?, work: Intent) {
        context?.let { enqueueWork(it, ClipJobIntentService::class.java, JOB_ID, work) }
    }
    override fun onHandleWork(p0: Intent) {
        Toast.makeText(applicationContext,"service is here", Toast.LENGTH_SHORT).show()

        thread = Thread(Runnable {
            //clipboard.addPrimaryClipChangedListener(primaryClipChangedListener)
            applicationContext.startService(Intent(baseContext, ClipBoardService::class.java))
            //jobFinished(job,false)
        })
        thread.start()
    }
}