package com.example.clipboardmanager.service.model

import android.arch.persistence.room.TypeConverter
import java.util.*


class Converter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return (date?.time)
    }
}