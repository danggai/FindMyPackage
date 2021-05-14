package danggai.app.parcelwhere.data.api

import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.data.local.Tracks
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("carriers/")
    fun carriers(): Observable<Response<List<Carrier>>>

    @GET("carriers/{carrId}/tracks/{trackId}")
    fun carriersTracks(@Path("carrId") carrierId: String, @Path("trackId") trackId: String): Observable<Response<Tracks>>

    @GET("carriers/{carrId}/tracks/{trackId}")
    suspend fun suspendCarriersTracks(@Path("carrId") carrierId: String, @Path("trackId") trackId: String): Response<Tracks>

}