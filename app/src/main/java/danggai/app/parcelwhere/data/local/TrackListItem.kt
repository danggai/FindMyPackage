package danggai.app.parcelwhere.data.local

import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.util.NonNullMutableLiveData

class TrackListItem (
    var trackEntity: TrackEntity,
    var isRefreshing: Boolean

//    var isRefreshing: NonNullMutableLiveData<Boolean>,
//    var isRefreshed: NonNullMutableLiveData<Boolean>,
    ) {

}