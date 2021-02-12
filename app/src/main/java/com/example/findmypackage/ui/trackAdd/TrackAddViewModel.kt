package com.example.findmypackage.ui.trackAdd

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.R
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.data.AppSession
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackAddViewModel(override val app: Application, private val api: ApiRepository) : BaseViewModel(app) {

    var lvPostNum: MutableLiveData<String> = MutableLiveData("")
    var lvCarrierId: MutableLiveData<String> = MutableLiveData("")

    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()

    private var _lvCarrierList: MutableLiveData<List<Carrier>> = MutableLiveData(listOf())
    val lvCarrierList = _lvCarrierList

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            rxApiCarrierTracks
                .observeOn(Schedulers.newThread())
                .switchMap {
                    log.e()
                    api.carriersTracks(it.first, it.second)
                }
                .subscribe({ res ->
                    log.e(res)
                }, {
                    it.message?.let { msg -> log.e(msg) }
                })
        )

        _lvCarrierList.value = AppSession.getCarrierList()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_search -> {
                log.d()
                trackPost()
            }
            else -> {
                log.e()
            }
        }
    }

    fun onClickItem(view: Carrier) {
        log.e(view.id)
        lvCarrierId.value = if (view.idx == -1) {
            ""
        } else {
            view.id
        }
    }

    private fun trackPost() {
        when {
            lvPostNum.value?.isNotEmpty()?:false && lvCarrierId.value?.isNotEmpty()?:false -> {
                rxApiCarrierTracks.onNext(Pair(lvCarrierId.value!!, lvPostNum.value!!))
            } lvCarrierId.value?.isEmpty() == true -> {
                lvMakeToast.value = getString(R.string.msg_carrier_empty)
            } lvPostNum.value?.isEmpty() == true -> {
                lvMakeToast.value = getString(R.string.msg_post_empty)
            } else -> {
                log.e()
            }
        }
    }

}