package com.example.clipboardmanager.viewModel

import android.app.AlertDialog
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.widget.Adapter
import com.example.clipboardmanager.service.repository.ClipBoardRepository
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.view.ui.MainActivity

class ClipBoardListViewModel(val context: Context) : ViewModel() {

    private var repository: ClipBoardRepository = ClipBoardRepository(context.applicationContext)

    fun delete(clipboardEntity: ClipboardEntity){
        repository.delete(clipboardEntity)
    }


    fun getAllNotes(): LiveData<List<ClipboardEntity>>{
        return repository.getAllNotes()
    }

    fun updateNote(clipboardEntity: ClipboardEntity){
        repository.update(clipboardEntity)
    }

    fun loadData(item_id: Int): LiveData<ClipboardEntity>{
        return repository.loadDataById(item_id)
        //return data_item
    }

    fun insertNote(clipboardEntity: ClipboardEntity){
        repository.insert(clipboardEntity)
    }

    /*fun getAdapter(): Adapter{

    }*/
    fun getNoteAtPosition(position:Int): String?{
        return getAllNotes().value?.get(position)?.note
    }

    fun getTimeAtPosition(position: Int): Long?{
        return getAllNotes().value?.get(position)?.date?.time
    }

    /*fun createDialog(context: Context,clipboardEntity: ClipboardEntity) {
        val position: Int = clipboardEntity.id!!

        //Toast.makeText(context, position,LENGTH_LONG).show()

        val alertDialogBuilder = AlertDialog.Builder(context.applicationContext)
        val options = arrayOfNulls<String>(2)
        if (clipboardEntity.note.length > 15) {
            options[0] = "Edit  " + clipboardEntity.note.substring(0, 10) + "..."
            options[1] = "Delete  " + clipboardEntity.note.substring(0, 10) + "..."
        } else {
            options[0] = "Edit " + clipboardEntity.note
            options[1] = "Delete " + clipboardEntity.note
        }
        alertDialogBuilder.setItems(options) { dialog, which ->
            if (which == 0) {
                (context as MainActivity).show(position, clipboardEntity)
            } else if (which == 1) {
                delete(clipboardEntity)
            }
        }
        alertDialogBuilder.create().show()
    }*/

}



