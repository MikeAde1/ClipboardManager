package com.example.clipboardmanager.service.repository

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.clipboardmanager.service.model.ClipBoardDao
import com.example.clipboardmanager.service.model.ClipDatabase
import com.example.clipboardmanager.service.model.ClipboardEntity

public class ClipBoardRepository(application: Application)  {
    private val clipDao: ClipBoardDao

    private val allNotes: LiveData<List<ClipboardEntity>>

    private var note_list2: List<ClipboardEntity>? = ArrayList()

    private lateinit var note: MutableList<ClipboardEntity>


    init {
        val clipDatabase: ClipDatabase = ClipDatabase.getInstance(application.applicationContext)!!
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

    fun getItembyNote(note: String): List<ClipboardEntity>? {
        return note_list2
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

    internal class LoadNoteAsyncTask(copiedDao: ClipBoardDao):  AsyncTask<Unit, Unit, List<ClipboardEntity>>(){
        var note: List<ClipboardEntity> = emptyList()
        val copied_dao = copiedDao
        override fun doInBackground(vararg params: Unit?): List<ClipboardEntity> {
           return copied_dao.selectNotes()
        }
        override fun onPostExecute(result: List<ClipboardEntity>) {
             note = result
        }
    }

    fun loadNotes(): List<ClipboardEntity>{
        //note = clipDao.selectNotes()
        return LoadNoteAsyncTask(clipDao).execute().get()
        /*Log.d("####", note.toString())
        return note*/
    }

    /*
    companion object {
        class LoadNoteAsyncTask(clipBoardRepositor: ClipBoardDao,
                                private val clipBoardRepository: ClipBoardRepository
        ):  AsyncTask<Unit, Unit, List<String>>(){
            val copied_dao = clipBoardRepository
            override fun doInBackground(vararg params: Unit?): List<String> {
                return copied_dao.selectNotes()
            }

            override fun onPostExecute(result: List<String>) {
                super.onPostExecute(result)
                clipBoardRepository.note = result
            }
        }
    }*/
}
