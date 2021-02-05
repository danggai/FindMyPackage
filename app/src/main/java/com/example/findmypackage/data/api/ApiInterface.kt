package com.example.findmypackage.data.api

import com.example.findmypackage.data.res.ResCarrier
import com.example.findmypackage.data.res.ResTracks
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("carriers/")
    fun carriers(): Observable<Response<Array<ResCarrier>>>

    @GET("carriers/:{carrId}/tracks/:{trackId}")
    fun carriersTracks(@Path("carrId") carrierId: String, @Path("trackId") trackId: String): Observable<Response<ResTracks>>

}