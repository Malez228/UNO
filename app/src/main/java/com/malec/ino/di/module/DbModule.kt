package com.malec.ino.di.module

import android.content.Context
import com.malec.ino.service.localDb.RoomsDao
import com.malec.ino.service.localDb.RoomsDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ContextModule::class])
class DbModule {
	@Provides
	@Singleton
	fun instance(context: Context): RoomsDatabase = RoomsDatabase.instance(context)

	@Provides
	@Singleton
	fun roomsDao(db: RoomsDatabase): RoomsDao = db.roomsDataDao()
}