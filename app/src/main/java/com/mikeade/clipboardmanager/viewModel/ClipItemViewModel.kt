package com.mikeade.clipboardmanager.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.mikeade.clipboardmanager.R
import com.mikeade.clipboardmanager.service.model.ClipboardEntity
import com.mikeade.clipboardmanager.utils.CustomDateUtils
import com.mikeade.clipboardmanager.view.ui.MainActivity

class ClipItemViewModel( val clipboardEntity: ClipboardEntity): ViewModel(){
    val text = MutableLiveData<String>()
    val dateText = MutableLiveData<String>()
    val id = MutableLiveData<Int>()

    init {
        text.value = clipboardEntity.note
        dateText.value = CustomDateUtils.format(clipboardEntity.date)
    }

    fun createDialog(context : Context) {
        (context as MainActivity).show(clipboardEntity)
    }

   val imageResId = R.drawable.ic_push_pin

    companion object{

    @BindingAdapter("android:imageSrc")
        @JvmStatic
        fun loadImage(view: ImageView, resId :Int) {
                view.setImageResource(resId)
        }
    }
}

