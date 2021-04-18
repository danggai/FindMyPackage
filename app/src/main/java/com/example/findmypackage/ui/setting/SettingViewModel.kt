package com.example.findmypackage.ui.setting

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.util.Event
import com.example.findmypackage.util.NonNullMutableLiveData
import com.example.findmypackage.util.log


class SettingViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartAccessNotiSetting = MutableLiveData<Event<Boolean>>()
    var lvIsAllowAccessNoti: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)         // denied: -1, granted: 0

    var lvStartGetNotiSetting = MutableLiveData<Event<Boolean>>()
    var lvIsAllowGetNoti: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)

    var lvAppVersion: NonNullMutableLiveData<String> = NonNullMutableLiveData("1.0.0")

    init {

    }

    fun onClickAccessNotiSwitch() {
        log.e()
        lvStartAccessNotiSetting.value = Event(true)
    }

    fun onClickGetNotiSwitch() {
        log.e()
        lvStartGetNotiSetting.value = Event(lvIsAllowGetNoti.value)
    }

}