package danggai.app.parcelwhere.data.api

import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.data.local.Tracks
import danggai.app.parcelwhere.data.res.*
import danggai.app.parcelwhere.util.log
import io.reactivex.Observable

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
        val emptyData = Tracks(trackId, Tracks.From("",""), Tracks.To("",""), Tracks.State("",""), listOf(), Tracks.Carrier("","",""))
        return Observable.just(true)
            .switchMap {
                api.carriersTracks(carrierId, trackId)
            }
            .map { res ->
                when {
                    res.isSuccessful -> {
                        res.body()?.let { data ->
                            if (data.progresses.size > 1 && data.progresses[0].time > data.progresses[1].time) {
                                log.e()
                                data.state.text = data.progresses[0].status.text
                                data.progresses = data.progresses.reversed()
                            }
                        }
                        var data = res.body()
                        data?.trackId = trackId
                        ResTracks(Meta(res.code(), res.message()), data?:emptyData)
                    } else -> {
                        ResTracks(Meta(res.code(), res.message()), emptyData)
                    }
                }
            }
    }

    suspend fun suspendCarriersTracks(carrierId: String, trackId: String): ResTracks {
        val emptyData = Tracks(trackId, Tracks.From("",""), Tracks.To("",""), Tracks.State("",""), listOf(), Tracks.Carrier("","",""))

        val res = api.suspendCarriersTracks(carrierId, trackId)
        when {
            res.isSuccessful -> {
                res.body()?.let { data ->
                    if (data.progresses.size > 1 && data.progresses[0].time > data.progresses[1].time) {
                        log.e()
                        data.state.text = data.progresses[0].status.text
                        data.progresses = data.progresses.reversed()
                    }
                }
                var data = res.body()
                data?.trackId = trackId
                return ResTracks(Meta(res.code(), res.message()), data ?: emptyData)
            }
            else -> {
                return ResTracks(Meta(res.code(), res.message()), emptyData)
            }
        }
    }
}