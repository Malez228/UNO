package com.malec.ino.service.network

import com.malec.ino.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomsApi {
	fun getRooms(): Flow<RoomResult>

	fun createRoom(room: Room)
}