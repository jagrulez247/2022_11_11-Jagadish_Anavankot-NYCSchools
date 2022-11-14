package com.education.nycschools.uicomponents.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.education.nycschools.uicomponents.R

fun View?.sendSmsTo(phone: String?) = phone?.takeIf { it.isNotBlank() }?.let {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("smsto:${Uri.encode(phone)}")
    }
    try {
        val context = this?.context
        context?.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.send_sms_chooser_title)
            )
        )
    } catch (ex: Exception) {
    }
}

fun View?.sendEmailTo(emailId: String?) = emailId?.takeIf { it.isNotBlank() }?.let {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(it))
    }
    try {
        val context = this?.context
        context?.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.send_email_chooser_title)
            )
        )
    } catch (ex: Exception) {
    }
}

fun View?.openMap(address: String?) = address?.takeIf { it.isNotBlank() }?.let {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("geo:0,0?q=$address")
    }
    try {
        val context = this?.context
        context?.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.navigate_address_chooser_title)
            )
        )
    } catch (ex: Exception) {
    }
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}


fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.showKeyboardForced() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}