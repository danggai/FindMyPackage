package com.example.findmypackage.util

import com.example.findmypackage.Constant
import java.text.SimpleDateFormat
import java.util.*

object CommonFuntion {

    fun now(): String {
        return SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).format(Date())
    }

    fun convertStateString(string: String): String {
        TODO()
        when (string) {

        }
        return ""
    }

    fun convertDateString(string: String): String {
        val beforeDate: Date? = SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(string)
        beforeDate?.let { return SimpleDateFormat(Constant.DATE_FORMAT_AFTER).format(it) }
        return "날짜 정보가 없습니다."
    }


}