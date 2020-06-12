package com.malec.ino.repo

import com.malec.ino.model.Room
import com.malec.ino.model.User
import com.malec.ino.service.network.RoomAction
import com.malec.ino.service.network.RoomResult
import com.malec.ino.service.network.RoomsApi
import kotlinx.coroutines.flow.map
import java.util.*

class RoomRepo(private val api: RoomsApi) {
	fun next() = api.getRooms().map {
		//10 min
		if (Date().time - it.room.time > 600000) {
			remove(it.room)
			RoomResult(it.room, RoomAction.REMOVE)
		} else it
	}

	fun create(name: String, pass: String, size: Int) {
		val roomName = if (name.isEmpty()) "Room" + (100000 .. 999999).random() else name
		val roomPass = if (pass.isEmpty()) "0" else pass

		val time = Date().time

		val room = Room(
			roomName, roomPass, 0, size, 0, time, listOf()
		)
		api.createRoom(room)
	}

	fun enter(room: Room, user: User) {
		api.enterRoom(room, user)
	}

	fun remove(room: Room) {
		api.removeRoom(room)
	}
}