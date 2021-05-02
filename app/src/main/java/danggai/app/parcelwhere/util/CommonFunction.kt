package danggai.app.parcelwhere.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
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
        val keywords: MutableList<String> = mutableListOf("송장", "배송")
        for (keyword in keywords) {
            if (string.contains(keyword)) {
                val substring = string.substring(string.indexOf(keyword) + keyword.length + 1)
                for (str in substring.split("/", ":", "\n")) {
                    val number = str.replace(Regex(Constant.PATTERN_NUM_ONLY), "")
                    if (number.length in 9..14 &&
                        !number.startsWith("010")) {
                        log.e(number)
                        return number
                    }
                }
            }
        }
        return ""
    }

    fun getItemName(string: String): String {
        val keywords: MutableList<String> = mutableListOf("상품명", "물품명")
        for (keyword in keywords) {
            if (string.contains(keyword)) {
                val substring = string.substring(string.indexOf(keyword) + keyword.length + 1)
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

    fun sendNotification(id: Int, context: Context, notiManager: NotificationManager, pendingIntent: PendingIntent?, msg: String) {
        var builder = NotificationCompat.Builder(context, Constant.PUSH_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_truck)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiManager.createNotificationChannel(
                NotificationChannel(Constant.PUSH_CHANNEL_ID, Constant.PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                    description = Constant.PUSH_CHANNEL_DESC
                }
            )
        }

        notiManager.notify(id, builder.build())
    }
}