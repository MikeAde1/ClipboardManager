package com.mikeade.clipboardmanager.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikeade.clipboardmanager.R
import com.mikeade.clipboardmanager.service.model.ClipboardEntity
import android.os.Build
import android.support.annotation.RequiresApi
import com.mikeade.clipboardmanager.databinding.ItemNotesBinding
import com.mikeade.clipboardmanager.viewModel.ClipItemViewModel


class ClipBoardAdapter(ctx: Context?, var noteList: List<ClipboardEntity>)
    :RecyclerView.Adapter<ClipBoardAdapter.ClipHolder>() {

    var context: Context? = ctx

    //var callbacks: Callback = callback

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ClipHolder {
        val notesBinding = DataBindingUtil.inflate<ItemNotesBinding>(LayoutInflater.from(viewGroup.context),
            R.layout.item_notes, viewGroup, false)
        return ClipHolder(notesBinding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onBindViewHolder(clipHolder:ClipHolder, pos: Int) {
        val position  = noteList[pos]
        clipHolder.bind(position)
    }

    fun setNotes(note: List<ClipboardEntity>){
         this.noteList = note
         notifyDataSetChanged()
    }

    fun getNoteAt(pos: Int): ClipboardEntity{
        return noteList[pos]
    }

    inner class ClipHolder( val notesBinding: ItemNotesBinding) :
        RecyclerView.ViewHolder(notesBinding.root) {

        /*override fun onClick(v: View?) {
            val id = noteList.get(adapterPosition)
            callbacks.onItemClick(id)
        }*/
        fun bind(clipboardEntity: ClipboardEntity){
           //notesBinding.viewModel = context?.applicationContext?.let { ClipBoardListViewModel(it) }
           notesBinding.viewModel = ClipItemViewModel(clipboardEntity)
           notesBinding.executePendingBindings()
        }
        /*val copied_txt = view.findViewById<TextView>(R.id.copied_txt)
        val saved_date = view.findViewById<TextView>(R.id.time)*/
        //val v = view.setOnClickListener(this)
    }

    interface Callback{
        fun onItemClick(clipboardEntity: ClipboardEntity)
    }

}