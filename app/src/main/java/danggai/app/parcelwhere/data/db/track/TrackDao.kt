package danggai.app.parcelwhere.data.db.track

import androidx.room.*
import io.reactivex.Observable

@Dao
interface TrackDao {
    @Query("SELECT * FROM Track")
    fun selectAll(): Observable<List<TrackEntity>>

    @Query("SELECT DISTINCT * FROM Track WHERE trackId = :id")
    fun selectById(id: String): Observable<TrackEntity>

    @Query("SELECT EXISTS(SELECT DISTINCT * FROM Track WHERE trackId = :id)")
    fun existById(id: String): Observable<Boolean>

    @Query("SELECT DISTINCT item_name FROM Track WHERE trackId = :id")
    fun selectItemNameById(id: String): String

    @Query("DELETE FROM Track WHERE trackId = :id")
    fun deleteById(id: String)

    @Query("UPDATE Track SET item_name = :name WHERE trackId = :id")
    fun updateNameById(name: String, id: String)

    @Query("UPDATE Track SET is_refreshed = :isRefreshed WHERE trackId = :id")
    fun updateRefreshedById(isRefreshed: Boolean, id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWithReplace(item: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: TrackEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(item: TrackEntity)
}