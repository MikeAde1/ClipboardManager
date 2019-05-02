package com.example.clipboardmanager.service.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*




    @Dao
    interface ClipBoardDao{

        @Query("SELECT * FROM word_table ORDER BY id DESC")
        fun getAll(): LiveData<List<ClipboardEntity>>

        @Insert(onConflict = OnConflictStrategy.FAIL)
        fun insert(todo: ClipboardEntity)

        @Delete
        fun delete(clip: ClipboardEntity)

        @Update
        fun update(clip: ClipboardEntity)

        @Query("SELECT * FROM word_table WHERE id = :id")
        fun loadDataById(id: Int): LiveData<ClipboardEntity>

        @Query("SELECT * FROM word_table")
        fun getItemByNote(): List<ClipboardEntity>?

        @Query("SELECT note, time, duplicated FROM word_table")
        fun selectNotes(): List<ClipboardEntity>
    }
