package com.example.clipboardmanager.viewModel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.example.clipboardmanager.service.model.ClipDatabase
import kotlin.Suppress as Suppress1


class ClipBoardViewModelFactory(val context: Context): ViewModelProvider.NewInstanceFactory() {



    @kotlin.Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClipBoardListViewModel(context.applicationContext) as T
    }
}