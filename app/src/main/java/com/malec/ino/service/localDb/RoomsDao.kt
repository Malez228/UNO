package com.malec.ino.service.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.malec.ino.model.Room
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomsDao {
	@Query("SELECT * FROM Room")
	fun getAll(): Flow<List<Room>>

	@Query("SELECT * FROM Room WHERE name LIKE :searchQuery")
	fun getTitleContains(searchQuery: String): Flow<List<Room>>

	@Insert(onConflict = REPLACE)
	fun insert(room: Room)

	@Insert(onConflict = REPLACE)
	fun insertAll(rooms: List<Room>)

	@Update
	fun update(room: Room)

	@Query("DELETE from Room")
	fun deleteAll()
}