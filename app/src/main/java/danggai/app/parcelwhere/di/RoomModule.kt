package danggai.app.parcelwhere.di

import danggai.app.parcelwhere.data.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * @author Leopold
 */
val RoomModule = module {
    single { AppDatabase.getInstance(androidApplication()) }
    single(createdAtStart = true) { get<AppDatabase>().trackDao() }
}