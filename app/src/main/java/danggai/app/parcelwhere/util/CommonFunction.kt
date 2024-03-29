package danggai.app.parcelwhere.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.worker.RefreshWorker
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object CommonFunction {

    fun now(): String {
        return SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).format(Date())
    }

    fun startOneTimeRefreshWorker(context: Context) {
        if (!PreferenceManager.getBooleanAutoRefresh(context)) return
        log.e()

        val workManager = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequestBuilder<RefreshWorker>()
            .setInputData(workDataOf(Constant.ARG_IS_ONE_TIME to true))
            .build()
        workManager.enqueue(workRequest)
    }

    fun startUniquePeriodicRefreshWorker(context: Context) {
        val period = PreferenceManager.getLongAutoRefreshPeriod(context)

        startUniquePeriodicRefreshWorker(context, period)
    }

    fun startUniquePeriodicRefreshWorker(context: Context, period: Long) {
        if (!PreferenceManager.getBooleanAutoRefresh(context)) return
        log.e("period -> $period")

        val workManager = WorkManager.getInstance(context)
        val workRequest = PeriodicWorkRequestBuilder<RefreshWorker>(period, TimeUnit.MINUTES)
            .setInitialDelay(period, TimeUnit.MINUTES)
            .setInputData(workDataOf(Constant.ARG_IS_ONE_TIME to false))
            .build()
        workManager.enqueueUniquePeriodicWork(Constant.WORKER_UNIQUE_NAME_AUTO_REFRESH, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
    }

    fun cancellAllWorkers(context: Context) {
        log.e()
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()
    }

    fun getTrackId(string: String): String {
        val keywords: MutableList<String> = mutableListOf("송장", "배송")
        for (keyword in keywords) {
            if (string.contains(keyword)) {
                val substring = string.substring(string.indexOf(keyword) + keyword.length)
                for (str in substring.split("/", ":", "\n")) {
                    val number = str.replace(Regex(Constant.PATTERN_NUM_ONLY), "")
                    if (number.length in 9..14 &&
                        !number.startsWith("0")) {
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
        return Constant.DEFAULT_ITEM_NAME
    }

    fun convertDateString(string: String): String {
        try {
            val beforeDate: Date? = SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(string)
            beforeDate?.let { return SimpleDateFormat(Constant.DATE_FORMAT_AFTER).format(it) }
        } catch (e: Exception) {
            return Constant.DEFAULT_DATE
        }
        return Constant.DEFAULT_DATE
    }

    fun sendNotification(id: Int, context: Context, pendingIntent: PendingIntent?, title: String, msg: String) {
        val notificationManager: NotificationManager = getSystemService(context, NotificationManager::class.java) ?: return

        var builder = NotificationCompat.Builder(context, Constant.PUSH_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_truck)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(Constant.PUSH_CHANNEL_ID, Constant.PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                    description = Constant.PUSH_CHANNEL_DESC
                }
            )
        }

        notificationManager.notify(id, builder.build())
    }
}