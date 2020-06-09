package com.malec.ino.service.network

import com.malec.ino.model.Room

class RoomResult(val room: Room, val action: RoomAction)

enum class RoomAction {
	ADD, UPDATE, REMOVE
}