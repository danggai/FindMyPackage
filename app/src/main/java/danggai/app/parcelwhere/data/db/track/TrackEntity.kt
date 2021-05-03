package danggai.app.parcelwhere.data.db.track

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Track")
data class TrackEntity (
    @PrimaryKey val trackId: String,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "from_name") val fromName: String?,
    @ColumnInfo(name = "carrier_id") val carrierId: String,
    @ColumnInfo(name = "carrier_name") val carrierName: String,
    @ColumnInfo(name = "recent_time") val recentTime: String?,
    @ColumnInfo(name = "recent_status") val recentStatus: String?,
    @ColumnInfo(name = "is_refreshed") var isRefreshed: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readInt() != 0
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(itemName)
        parcel.writeString(fromName)
        parcel.writeString(carrierId)
        parcel.writeString(carrierName)
        parcel.writeString(recentTime)
        parcel.writeString(recentStatus)
        parcel.writeInt(if(isRefreshed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackEntity> {
        override fun createFromParcel(parcel: Parcel): TrackEntity {
            return TrackEntity(parcel)
        }

        override fun newArray(size: Int): Array<TrackEntity?> {
            return arrayOfNulls(size)
        }
    }

}