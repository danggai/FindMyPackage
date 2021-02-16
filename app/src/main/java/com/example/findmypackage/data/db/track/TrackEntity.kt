package com.example.findmypackage.data.db.track

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
)