package com.example.natour.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.natour.R
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

fun showCustomAlertDialog(title: String, message: String, context: Context) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Okay") { _, _ -> }
        .show()
}

fun createProgressAlertDialog(message: String, context: Context): AlertDialog =
    AlertDialog.Builder(context)
        .setCancelable(false)
        .setView(progressAlertDialogView(message, context))
        .create()

private fun progressAlertDialogView(message: String, context: Context): View {
    val root = View.inflate(context, R.layout.progress_dialog_layout, null)
    val messageTextView = root.findViewById<TextView>(R.id.message_text_view)
    messageTextView.text = message
    return root
}

fun showSomethingWentWrongAlertDialog(title: String, message: String, context: Context) {
    AlertDialog.Builder(context)
        .create()
        .apply {
            setView(somethingWentWrongAlertDialogView(this, title, message, context))
            show()
        }
}

private fun somethingWentWrongAlertDialogView(
    alertDialog: AlertDialog,
    title: String,
    message: String,
    context: Context
): View {
    val root = View.inflate(context, R.layout.something_went_wrong_dialog_layout, null)
    val errorTitleTextView = root.findViewById<TextView>(R.id.error_title_text_view)
    val messageTextView = root.findViewById<TextView>(R.id.message_text_view)
    val closeButton = root.findViewById<ImageButton>(R.id.close_button)
    errorTitleTextView.text = title
    messageTextView.text = message
    closeButton.setOnClickListener { alertDialog.dismiss() }
    return root
}