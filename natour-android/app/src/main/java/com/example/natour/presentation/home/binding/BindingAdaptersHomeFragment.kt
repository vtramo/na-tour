package com.example.natour.presentation.home.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.natour.user.MainUser

@BindingAdapter("mainUser")
fun bindMainUser(textView: TextView, mainUser: MainUser) {
    textView.text = mainUser.toString()
}
