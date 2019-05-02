package com.example.clipboardmanager.service.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "word_table")
data class ClipboardEntity(@PrimaryKey(autoGenerate = true) var id: Int?= null,
                           @ColumnInfo (name = "note") var note: String,
                           @ColumnInfo (name = "time") var time: String = "",
                            @ColumnInfo (name = "duplicated") var number: Int = 0 ){

}