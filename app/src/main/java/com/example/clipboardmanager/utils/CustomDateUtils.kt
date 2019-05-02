package com.example.clipboardmanager.utils

import android.text.format.DateUtils
import android.util.Log
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import javax.xml.datatype.DatatypeConstants.DAYS



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
    }
}