package com.mikeade.clipboardmanager.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mikeade.clipboardmanager.R
import com.mikeade.clipboardmanager.service.model.ClipboardEntity

class FloatingAdapter(val floatingClickListener: FloatingClickListener) : RecyclerView.Adapter<FloatingAdapter.NotesHolder>() {

    private var clipList: List<ClipboardEntity> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): NotesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_floating_icon, parent, false)
        return NotesHolder(view)
    }

    inner class NotesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txt_notes)
        val imageView: ImageView = view.findViewById(R.id.txt_icon)

    }

    fun setNotes(list: MutableList<ClipboardEntity>){
       this.clipList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return clipList.size
    }

    override fun onBindViewHolder(holder: NotesHolder, pos: Int) {
        holder.textView.text = clipList[pos].note
        holder.textView.setOnClickListener{
            floatingClickListener.clickListener(clipList[pos])
        }
    }

    interface FloatingClickListener{
        fun clickListener(clipboardEntity: ClipboardEntity)
    }
}