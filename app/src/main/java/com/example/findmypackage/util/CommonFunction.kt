package com.example.findmypackage.util

import com.example.findmypackage.Constant
import java.text.SimpleDateFormat
import java.util.*

object CommonFunction {

    fun now(): String {
        return SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).format(Date())
    }

    fun convertState(string: String): String {
        TODO()
        when (string) {

        }
        return ""
    }

    fun getTrackId(string: String): String {
        if (string.contains("송장")) {
            for (line in string.split("\n")) {
                if (line.contains("송장")) {
                    return line.replace(Regex(Constant.PATTERN_NUM_ONLY), "")
                }
            }
        }
        return ""
    }

    fun convertDateString(string: String): String {
        val beforeDate: Date? = SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(string)
        beforeDate?.let { return SimpleDateFormat(Constant.DATE_FORMAT_AFTER).format(it) }
        return "날짜 정보가 없습니다."
    }
}