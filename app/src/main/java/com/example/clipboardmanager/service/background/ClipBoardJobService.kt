package com.example.clipboardmanager.service.background


import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.service.repository.ClipBoardRepository
import com.firebase.jobdispatcher.JobParameters

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ClipBoardJobService: com.firebase.jobdispatcher.JobService() {

    lateinit var clipboard : ClipboardManager

    private lateinit var clipBoardRepository: ClipBoardRepository

    private var mPreviousText=""

    private lateinit var thread:Thread

    override fun onCreate() {
        super.onCreate()
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardRepository = ClipBoardRepository(application)
    }
    override fun onStopJob(job: JobParameters): Boolean {
        Toast.makeText(applicationContext,"service ended!!!", LENGTH_SHORT).show()
        return true
    }

    override fun onStartJob(job: JobParameters): Boolean {
        Toast.makeText(applicationContext,"service is here", LENGTH_SHORT).show()

        thread = Thread(Runnable {
            //clipboard.addPrimaryClipChangedListener(primaryClipChangedListener)
            applicationContext.startService(Intent(baseContext, ClipBoardService::class.java))
            //jobFinished(job,false)
        })
        thread.start()
        return true
    }



    override fun onDestroy() {
        super.onDestroy()
        //Toast.makeText(applicationContext, "Job Service Destroyed", LENGTH_SHORT).show()
        //InsertionUtils().scheduleChargingReminder(applicationContext)
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
        Toast.makeText(applicationContext, "New note copied: $pasteData \n Note has been copied $copies times" ,
            Toast.LENGTH_SHORT
        ).show()
    }

    /*private class LoadService(var context: Context) :AsyncTask<Unit,Unit,Unit>(){
        override fun doInBackground(vararg params: Unit?) {
            context.startService(Intent(con))
        }
        val primaryClipChangedListener = object: ClipboardManager.OnPrimaryClipChangedListener{
            override fun onPrimaryClipChanged() {
                var pasteData: String = ""
                if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription()!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    val item = clipboard.primaryClip?.getItemAt(0)?.text
                    pasteData = item.toString()
                    checkForDuplicate(pasteData)

                }
            }
        }
    }*/
}