package com.example.clipboardmanager.utils

import android.text.format.DateUtils
import android.util.Log
import com.example.clipboardmanager.service.model.ClipboardEntity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import javax.xml.datatype.DatatypeConstants.DAYS
import kotlin.collections.ArrayList


class CustomDateUtils {
    companion object{
        fun isYesterday(time:Long): Boolean{
            return DateUtils.isToday(time + DateUtils.DAY_IN_MILLIS)
        }

        fun isMorethanOneWeek(longtime: Long): Boolean{
            val toDate = Calendar.getInstance().timeInMillis
            val fromDate = Date(longtime)
            val c=Calendar.getInstance()
            c.setTime(fromDate);
            c.add(Calendar.DATE,7);
            if(c.getTime().compareTo(Date(toDate))<0)return true

            return false
        }

        fun isLessThanTwoWeeks(longtime: Long): Boolean{
            val toDate = Calendar.getInstance().timeInMillis
            val fromDate = Date(longtime)
            val c=Calendar.getInstance()
            c.setTime(fromDate);
            c.add(Calendar.DATE,14);
            if(c.getTime().compareTo(Date(toDate))>0) return true

            return false
        }

        fun isLessThanThreeWeeks(longtime: Long): Boolean{
            val toDate = Calendar.getInstance().timeInMillis
            val fromDate = Date(longtime)
            val c=Calendar.getInstance()
            c.setTime(fromDate);
            c.add(Calendar.DATE,21)
            if(c.getTime().compareTo(Date(toDate))>0) return true

            return false
        }

        fun isLessThanFourWeeks(longtime: Long):Boolean{
            val toDate = Calendar.getInstance().timeInMillis
            val fromDate = Date(longtime)
            val c=Calendar.getInstance()
            c.setTime(fromDate);
            c.add(Calendar.DATE,28)
            if(c.getTime().compareTo(Date(toDate))>0) return true

            return false
        }

        fun isLessThanTwoMonths(longtime: Long):Boolean{
            val toDate = Calendar.getInstance().timeInMillis
            val fromDate = Date(longtime)
            val c=Calendar.getInstance()
            c.setTime(fromDate);
            c.add(Calendar.MONTH,2)

            if(c.getTime().compareTo(Date(toDate)) > 0) return true
            return false
        }

        fun isMoreThanTwoMonths(longtime: Long):Boolean{
            val toDate = Calendar.getInstance().timeInMillis
            val fromDate = Date(longtime)
            val c=Calendar.getInstance()
            c.setTime(fromDate);
            c.add(Calendar.MONTH,2)
            if(c.getTime().compareTo(Date(toDate)) < 0) return true
            return false
        }

        fun sortList(day:Int?, clipList: List<ClipboardEntity>): List<ClipboardEntity>{
            val filteredList: MutableList<ClipboardEntity> = ArrayList()
            val calendar = Calendar.getInstance()

            when (day) {
                1 -> {
                    calendar.add(Calendar.MONTH, -day)
                    val date = calendar.time
                    for (notes in 0 until clipList.size){
                        //if note taken was after one month ago
                        if ((clipList[notes].date).after(date)){
                            filteredList.add(clipList[notes])
                        }
                    }
                }
                20 -> {
                    calendar.add(Calendar.DATE, -day)
                    val date = calendar.time
                    for (notes in 0 until clipList.size){
                        if ((clipList[notes].date).after(date)){
                            filteredList.add(clipList[notes])
                        }
                    }
                }
                else -> for (notes in 0 until clipList.size){
                    filteredList.add(clipList[notes])
                }
            }
            return filteredList
        }

        @JvmStatic
        fun format(savedTime:Date): String{
            val hourFormat = SimpleDateFormat("hh:mm a",Locale.ENGLISH)
            val mtimeFormat = SimpleDateFormat("EEE, MMM d, yyyy HH:mm a", Locale.ENGLISH)
            var formattedTime = ""
            //val timeFormat = SimpletimeFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH)
            if (DateUtils.isToday(savedTime.time)){
                 formattedTime  = "Today, ${DateUtils.getRelativeTimeSpanString( savedTime.time)}"
                //p0.saved_date.text = formattedTime
            }else if (isYesterday(savedTime.time)){
                 formattedTime = "Yesterday, ${hourFormat.format(Date(savedTime.time))}"
                //p0.saved_date.text = formattedTime
            }else if (isMorethanOneWeek(savedTime.time) && CustomDateUtils.isLessThanTwoWeeks(savedTime.time)){
                 formattedTime = "A week ago, ${hourFormat.format(Date(savedTime.time))}"
                //p0.saved_date.text = formattedTime
            }else if (!isLessThanTwoWeeks(savedTime.time) && CustomDateUtils.isLessThanThreeWeeks(savedTime.time)){
                 formattedTime = "Two weeks ago, ${hourFormat.format(Date(savedTime.time))}"
                //p0.saved_date.text = formattedTime
            }else if (!isLessThanThreeWeeks(savedTime.time) && CustomDateUtils.isLessThanFourWeeks(savedTime.time)){
                 formattedTime = "Three weeks ago, ${hourFormat.format(Date(savedTime.time))}"
                //p0.saved_date.text = formattedTime
            }else if (!isLessThanFourWeeks(savedTime.time) && CustomDateUtils.isLessThanTwoMonths(savedTime.time)){
                 formattedTime = "One month ago, ${hourFormat.format(Date(savedTime.time))}"
                //p0.saved_date.text = formattedTime
            }else if (!isLessThanTwoMonths(savedTime.time) && CustomDateUtils.isMoreThanTwoMonths(savedTime.time)){
                 formattedTime = "Two months ago, ${hourFormat.format(Date(savedTime.time))}"
                //p0.saved_date.text = formattedTime
            }else{
                 formattedTime = mtimeFormat.format(Date(savedTime.time))
                //p0.saved_date.text = formattedTime
            }
            return formattedTime
        }
    }
}