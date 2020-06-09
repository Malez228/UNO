package com.malec.ino.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.malec.ino.service.network.FirebaseRoomsApi
import com.malec.ino.service.network.RoomsApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class NetworkModule {
	@Provides
	@Singleton
	fun database(): DatabaseReference = FirebaseDatabase.getInstance().reference

	@Provides
	@Singleton
	fun api(db: DatabaseReference): RoomsApi = FirebaseRoomsApi(db)
}