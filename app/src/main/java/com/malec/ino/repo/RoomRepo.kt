package com.malec.ino.repo

import com.malec.ino.model.Room
import com.malec.ino.service.network.RoomsApi

class RoomRepo(private val api: RoomsApi) {
	fun next() = api.getRooms()

	fun create(name: String, pass: String, size: Int) {
		val roomName = if (name.isEmpty()) "Room" + (100000 .. 999999).random() else name
		val roomPass = if (pass.isEmpty()) "0" else pass

		val room = Room(
			roomName, roomPass, 0, size, 0, 0, listOf()
		)
		api.createRoom(room)
	}
}