package com.example.findmypackage.data.api

import com.example.findmypackage.Constant
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.data.local.Tracks
import com.example.findmypackage.data.res.*
import com.example.findmypackage.util.log
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*

class ApiRepository(private val api: ApiInterface) {

    fun carriers(): Observable<ResCarrier> {
        val emptyData = listOf<Carrier>()
        return Observable.just(true)
            .switchMap {
                api.carriers()
            }
            .map { res ->
                when {
                    res.isSuccessful -> {
                        ResCarrier(Meta(res.code(), res.message()), res.body()?:emptyData)
                    } else -> {
                        ResCarrier(Meta(res.code(), res.message()), emptyData)
                    }
                }
            }
    }

    fun carriersTracks(carrierId: String, trackId: String): Observable<ResTracks> {
        val emptyData = Tracks(Tracks.From("",""), Tracks.To("",""), Tracks.State("",""), listOf(), Tracks.Carrier("","",""))
        return Observable.just(true)
            .switchMap {
                api.carriersTracks(carrierId, trackId)
            }
            .map { res ->
                when {
                    res.isSuccessful -> {
                        res.body()?.let { data ->
                            if (data.progresses.size > 1 &&
                                SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(data.progresses[0].time) > SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(data.progresses[1].time)) {
                                log.e()
                                data.state.text = data.progresses[0].status.text
                                data.progresses = data.progresses.reversed()
                            }
                        }
                        ResTracks(Meta(res.code(), res.message()), res.body()?:emptyData)
                    } else -> {
                        ResTracks(Meta(res.code(), res.message()), emptyData)
                    }
                }
            }
    }
}