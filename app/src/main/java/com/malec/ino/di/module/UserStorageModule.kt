package com.malec.ino.di.module

import android.content.Context
import com.malec.ino.localStorage.userStorage.UserSharedPref
import com.malec.ino.localStorage.userStorage.UserStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class UserStorageModule {
	@Provides
	@Singleton
	fun userStorage(context: Context): UserStorage = UserSharedPref(context)
}