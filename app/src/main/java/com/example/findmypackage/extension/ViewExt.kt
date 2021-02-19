package com.example.findmypackage.extension

import android.text.InputFilter
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern


fun AppCompatEditText.onlyEngNum() {
    this.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
        val ps: Pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        return@InputFilter if (!ps.matcher(source).matches()) {
            ""
        } else {
            null
        }
    })
}

fun AppCompatEditText.onlyEngCap() {
    this.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
}