package com.example.clipboardmanager.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.clipboardmanager.R
import com.example.clipboardmanager.service.model.ClipboardEntity
import com.example.clipboardmanager.utils.CustomDateUtils
import com.example.clipboardmanager.view.ui.MainActivity

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

