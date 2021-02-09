package com.example.findmypackage.data.db.postList

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_list")
class PostListEntity (
    @PrimaryKey val postNumber: Int,
    @ColumnInfo val itemName: String?,
    @ColumnInfo val fromName: String?,
    @ColumnInfo val carrierId: String,
    @ColumnInfo val carrierName: String,
    @ColumnInfo val recentTime: String?,
    @ColumnInfo val recentStatus: String?,
)