package com.example.findmypackage.data.api

import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.data.res.*
import com.example.findmypackage.util.log
import io.reactivex.Observable

class ApiRepository(private val api: ApiInterface) {

    fun carriers(): Observable<ResCarrier> {
        val emptyData = listOf<Carrier>()
        log.d()
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
        log.d()
        return Observable.just(true)
            .switchMap {
                api.carriersTracks(carrierId, trackId)
            }
            .map { res ->
                res.body()
            }
    }
}