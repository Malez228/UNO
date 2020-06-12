package com.malec.ino.service.network

import com.malec.ino.model.Room
import com.malec.ino.model.User
import kotlinx.coroutines.flow.Flow

interface RoomsApi {
	fun getRooms(): Flow<RoomResult>

	fun createRoom(room: Room)

	fun enterRoom(room: Room, user: User)

	fun removeRoom(room: Room)
}