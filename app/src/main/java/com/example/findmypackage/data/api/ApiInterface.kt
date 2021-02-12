package com.example.findmypackage.data.api

import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.data.local.Tracks
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("carriers/")
    fun carriers(): Observable<Response<List<Carrier>>>

    @GET("carriers/:{carrId}/tracks/:{trackId}")
    fun carriersTracks(@Path("carrId") carrierId: String, @Path("trackId") trackId: String): Observable<Response<ResTracks>>

}