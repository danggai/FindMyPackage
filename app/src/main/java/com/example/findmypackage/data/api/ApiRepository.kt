package com.example.findmypackage.data.api

import android.util.Log
import com.example.findmypackage.data.res.*
import com.example.findmypackage.util.log
import com.google.gson.Gson
import io.reactivex.Observable

class ApiRepository(private val api: ApiInterface) {

    fun carriers(): Observable<Array<ResCarrier>> {
        log.d()
        return Observable.just(true)
            .switchMap {
                api.carriers()
            }
            .map { res ->
                res.body()
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