package com.example.findmypackage.ui.setting

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.data.AppSession
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.util.Event
import com.example.findmypackage.util.NonNullMutableLiveData
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class SettingViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartNotiSetting = MutableLiveData<Event<Boolean>>()

    var lvIsAllowNotiPermission: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)         // denied: -1, granted: 0

    init {

    }

    fun onClickNotiSwitch() {
        log.e()
        lvStartNotiSetting.value = Event(true)
    }

}