package com.example.clipboardmanager.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.clipboardmanager.service.model.ClipDatabase
import kotlin.Suppress as Suppress1


public class ClipBoardViewModelFactory(val cDb: ClipDatabase, val mDataId: Int): ViewModelProvider.NewInstanceFactory() {


    @Suppress1("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClipBoardViewModelFactory(cDb, mDataId) as T
    }
}