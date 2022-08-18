package com.example.natour.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(text: String, view: View) {
    Snackbar.make(
        view,
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun showErrorAlertDialog(message: String, context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Error")
        .setMessage(message)
        .setPositiveButton("Okay") { _, _ -> }
        .show()
}