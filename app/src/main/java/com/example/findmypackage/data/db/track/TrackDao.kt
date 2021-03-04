package com.example.findmypackage.data.db.track

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface TrackDao {
    @Query("SELECT * FROM Track")
    fun selectAll(): Observable<List<TrackEntity>>

    @Query("SELECT DISTINCT * FROM Track WHERE trackId = :id")
    fun selectById(id: String): Observable<TrackEntity>

    @Query("SELECT DISTINCT item_name FROM Track WHERE trackId = :id")
    fun selectItemNameById(id: String): String

    @Query("DELETE FROM Track WHERE trackId = :id")
    fun deleteById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWithReplace(item: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: TrackEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(item: TrackEntity)
}