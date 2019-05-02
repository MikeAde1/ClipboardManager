package com.example.clipboardmanager.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.clipboardmanager.service.repository.ClipBoardRepository
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.view.ui.ClipBoardFragment

class ClipBoardListViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ClipBoardRepository = ClipBoardRepository(application)
    private lateinit var data_item: LiveData<ClipboardEntity>

    fun delete(clipboardEntity: ClipboardEntity){
        repository.delete(clipboardEntity)
    }

    fun getAllNotes(): LiveData<List<ClipboardEntity>>{
        val allnotes: LiveData<List<ClipboardEntity>> = repository.getAllNotes()
        return allnotes
    }

    fun updateNote(clipboardEntity: ClipboardEntity){
        repository.update(clipboardEntity)
    }

    fun loadData(item_id: Int): LiveData<ClipboardEntity>{
        data_item = repository.loadDataById(item_id)
        return data_item
    }

    /*fun getData(): LiveData<ClipboardEntity>{
        return data_item
    }*/


}



