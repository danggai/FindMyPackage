package com.example.findmypackage.util

import android.util.Log

object log {

    val TAG = "myLog"

    fun d () {
        val e = Exception()
        val element: Array<StackTraceElement> = e.stackTrace

        Log.d(TAG, "(" + element[1].fileName + ":" + element[1].lineNumber + ") " +  element[1].methodName)
    }

    fun d (msg: Any) {
        val e = Exception()
        val element: Array<StackTraceElement> = e.stackTrace

        Log.d(TAG, "(" + element[1].fileName + ":" + element[1].lineNumber + ") " +  element[1].methodName + ": " + msg.toString())
    }

    fun e () {
        val e = Exception()
        val element: Array<StackTraceElement> = e.stackTrace

        Log.e(TAG, "(" + element[1].fileName + ":" + element[1].lineNumber + ") " +  element[1].methodName)
    }

    fun e (msg: Any) {
        val e = Exception()
        val element: Array<StackTraceElement> = e.stackTrace

        Log.e(TAG, "(" + element[1].fileName + ":" + element[1].lineNumber + ") " +  element[1].methodName + ": " + msg.toString())
    }

}