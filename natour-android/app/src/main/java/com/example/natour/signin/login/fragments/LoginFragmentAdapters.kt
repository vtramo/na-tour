package com.example.natour.signin.login.fragments

import androidx.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton

// Delete this
@BindingAdapter("android:onClick")
fun bindSignInWithGoogleClick(button: SignInButton, method: () -> Unit) = method.invoke()