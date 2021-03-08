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

    fun getItemName(string: String): String {
        val keywords: MutableList<String> = mutableListOf("상품명")
        for (keyword in keywords) {
            if (string.contains(keyword)) {
                val substring = string.substring(string.lastIndexOf(keyword) + keyword.length + 1)
                val tokens = substring.split(":", "\n")
                log.e(tokens)
                for (name in tokens) {
                    if (name.length > keyword.length) {
                        log.e(name)
                        return name.trim()
                    }
                }
            }
        }
        return "자동 등록 된 물건"
    }

    fun convertDateString(string: String): String {
        val beforeDate: Date? = SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(string)
        beforeDate?.let { return SimpleDateFormat(Constant.DATE_FORMAT_AFTER).format(it) }
        return "날짜 정보가 없습니다."
    }
}