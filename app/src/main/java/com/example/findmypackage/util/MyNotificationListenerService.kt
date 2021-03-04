package com.example.findmypackage.util

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.findmypackage.data.db.AppDatabase
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.ui.main.MainViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MyNotificationListenerService: NotificationListenerService() {

    private val db = AppDatabase.getInstance(this)
    private val dao: TrackDao = db.getTrackListDao()

    private val rxDaoInsertWithIgnore: PublishSubject<TrackEntity> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add (
            rxDaoInsertWithIgnore
                .observeOn(Schedulers.newThread())
                .subscribe ({ item ->
                    dao.insertWithIgnore(item)
                    log.e(item)
                }, {
                    it.message?.let { msg -> log.e(msg) }
                })
        )
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        log.e("MyNotificationListener.onListenerConnected()")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        log.e("MyNotificationListener.onListenerDisconnected()")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.notification?.let {
            val text: CharSequence = it.extras.getCharSequence(Notification.EXTRA_TEXT)?:""

            val carrierId: String = CarrierUtil.getCarrierId(text.toString())
            val trackId: String = CommonFunction.getTrackId(text.toString())

            log.e("carrierId = $carrierId, trackId = $trackId")
            if ((trackId.length in 9..14 && CarrierUtil.checkCarrierId(carrierId))) {
                rxDaoInsertWithIgnore.onNext(
                    TrackEntity(trackId, "자동 등록 된 물건", "", carrierId, CarrierUtil.getCarrierName(carrierId), CommonFunction.now(), "")
                )
            }
        }
    }
}