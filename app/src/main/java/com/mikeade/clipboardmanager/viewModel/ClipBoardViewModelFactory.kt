package com.mikeade.clipboardmanager.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context


class ClipBoardViewModelFactory(val context: Context): ViewModelProvider.NewInstanceFactory() {



    @kotlin.Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClipBoardListViewModel(context.applicationContext) as T
    }
}