package com.example.clipboardmanager.service.repository

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.clipboardmanager.service.model.ClipBoardDao
import com.example.clipboardmanager.service.model.ClipDatabase
import com.example.clipboardmanager.service.model.ClipboardEntity

class ClipBoardRepository(context: Context) {
    private val clipDao: ClipBoardDao

    private val allNotes: LiveData<List<ClipboardEntity>>

    //private lateinit var note: MutableList<ClipboardEntity>


    init {
        val clipDatabase: ClipDatabase = ClipDatabase.getInstance(context.applicationContext)!!
        clipDao = clipDatabase.clipBoardDao()
        allNotes = clipDao.getAll()
    }

    fun insert(cEntity: ClipboardEntity){
       InsertCopiedNoteAsyncTask(clipDao).execute(cEntity)
    }

    fun getAllNotes(): LiveData<List<ClipboardEntity>> {
        return allNotes
    }

    fun delete(cEntity: ClipboardEntity){
        DeleteWordAsyncTask(clipDao).execute(cEntity)
    }

    fun update(cEntity: ClipboardEntity){
        UpdateWordAsyncTask(clipDao).execute(cEntity)
    }

    fun loadDataById(item_id: Int): LiveData<ClipboardEntity> {
       return clipDao.loadDataById(item_id)
    }

    private class InsertCopiedNoteAsyncTask(copiedDao: ClipBoardDao): AsyncTask<ClipboardEntity, Unit, Unit>(){
        val copied_dao = copiedDao
        override fun doInBackground(vararg params: ClipboardEntity) {
            copied_dao.insert(params[0])
        }
    }

    private class DeleteWordAsyncTask(copiedDao: ClipBoardDao):
        AsyncTask<ClipboardEntity, Unit, Unit>() {
        val copied_dao = copiedDao
        override fun doInBackground(vararg params: ClipboardEntity) {
            copied_dao.delete(params[0])
        }
    }

    private class UpdateWordAsyncTask(copiedDao: ClipBoardDao):
        AsyncTask<ClipboardEntity, Unit, Unit>(){
           val copied_dao = copiedDao
        override fun doInBackground(vararg params: ClipboardEntity) {
            copied_dao.update(params[0])
        }
    }

    private class LoadWordAsyncTask(copiedDao: ClipBoardDao):
        AsyncTask<Int, Unit, LiveData<ClipboardEntity>>(){
        val copied_dao = copiedDao
        override fun doInBackground(vararg params: Int?): LiveData<ClipboardEntity>? {
            return params[0]?.let { copied_dao.loadDataById(it) }
        }
    }

    //for unfiltered list of services
    private class LoadNoteAsyncTask(copiedDao: ClipBoardDao):  AsyncTask<Unit, Unit, LiveData<List<ClipboardEntity>>>(){
        val copied_dao = copiedDao
        override fun doInBackground(vararg params: Unit?): LiveData<List<ClipboardEntity>> {
           return copied_dao.selectNotes()
        }
    }

    fun loadNotes(): LiveData<List<ClipboardEntity>>{
        //note = clipDao.selectNotes()
        return LoadNoteAsyncTask(clipDao).execute().get()
        /*Log.d("####", note.toString())
        return note*/
    }


    //for background service
    fun getNotes(): List<ClipboardEntity>{
        return LoadAllNotesAsyncTask(clipDao).execute().get()
    }

    private class LoadAllNotesAsyncTask(copiedDao: ClipBoardDao):  AsyncTask<Unit, Unit, MutableList<ClipboardEntity>>(){
        val copied_dao = copiedDao
        override fun doInBackground(vararg params: Unit?): MutableList<ClipboardEntity> {
            return copied_dao.getNotes()
        }
    }

}
