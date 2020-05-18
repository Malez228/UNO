package com.malec.ino.service.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [com.malec.ino.model.Room::class], version = 1)
abstract class RoomsDatabase: RoomDatabase() {
	companion object {
		fun instance(context: Context): RoomsDatabase {
			return Room.databaseBuilder(
				context.applicationContext, RoomsDatabase::class.java, "roomsDb"
			).build()
		}
	}

	abstract fun roomsDataDao(): RoomsDao
}