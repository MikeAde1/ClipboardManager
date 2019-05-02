package com.example.clipboardmanager.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.clipboardmanager.R
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.utils.CustomDateUtils
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import android.support.annotation.RequiresApi


public class ClipBoardAdapter(ctx: Context?, var noteList: List<ClipboardEntity>, var callback: Callback)
    :RecyclerView.Adapter<ClipBoardAdapter.ClipHolder>() {


    var context: Context? = ctx

    var callbacks: Callback = callback

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClipHolder {
        val view = LayoutInflater.from(p0.context)
            .inflate(R.layout.notes, p0, false)
            return ClipHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onBindViewHolder(p0:ClipHolder, p1: Int) {
        val position  = noteList.get(p1)
        p0.copied_txt.text = position.note
        val time = position.time.toLong()
        val timeFormat = SimpleDateFormat("hh:mm:ss", Locale.ENGLISH)
        val hourFormat = SimpleDateFormat("hh:mm a",Locale.ENGLISH)
        val mtimeFormat = SimpleDateFormat("EEE, MMM d, yyyy HH:mm a", Locale.ENGLISH)
        //if ("EEE, MMM d, yyyy")
        //val timeFormat = SimpletimeFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH)
        if (DateUtils.isToday(position.time.toLong())){
            //today
            val formattedTime  = "Today, ${DateUtils.getRelativeTimeSpanString( time)}"
            p0.saved_date.text = formattedTime
        }else if (CustomDateUtils.isYesterday(time)){
            //yesterday
            val formattedTime = "Yesterday, ${hourFormat.format(Date(time))}"
            p0.saved_date.text = formattedTime
        }else if (CustomDateUtils.isMorethanOneWeek(time) && CustomDateUtils.isLessThanTwoWeeks(time)){
            val formattedTime = "A week ago, ${hourFormat.format(Date(time))}"
            p0.saved_date.text = formattedTime
        }else if (!CustomDateUtils.isLessThanTwoWeeks(time) && CustomDateUtils.isLessThanThreeWeeks(time)){
            val formattedTime = "Two weeks ago, ${hourFormat.format(Date(time))}"
            p0.saved_date.text = formattedTime
        }else if (!CustomDateUtils.isLessThanThreeWeeks(time) && CustomDateUtils.isLessThanFourWeeks(time)){
            val formattedTime = "Three weeks ago, ${hourFormat.format(Date(time))}"
            p0.saved_date.text = formattedTime
        }else if (!CustomDateUtils.isLessThanFourWeeks(time) && CustomDateUtils.isLessThanTwoMonths(time)){
            val formattedTime = "One month ago, ${hourFormat.format(Date(time))}"
            p0.saved_date.text = formattedTime
        }else if (!CustomDateUtils.isLessThanTwoMonths(time) && CustomDateUtils.isMoreThanTwoMonths(time)){
            val formattedTime = "Two months ago, ${hourFormat.format(Date(time))}"
            p0.saved_date.text = formattedTime
        }else{
            val formattedTime = mtimeFormat.format(Date(time))
            p0.saved_date.text = formattedTime
        }


    }

    fun setNotes(note: List<ClipboardEntity>){
         this.noteList= note
         notifyDataSetChanged()
    }

    inner class ClipHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View?) {
            val id = noteList.get(adapterPosition)
            callbacks.onItemClick(id)
        }

        val copied_txt = view.findViewById<TextView>(R.id.copied_txt)
        val saved_date = view.findViewById<TextView>(R.id.time)

        val v = view.setOnClickListener(this)

    }

    interface Callback{
        fun onItemClick(clipboardEntity: ClipboardEntity)
    }

}