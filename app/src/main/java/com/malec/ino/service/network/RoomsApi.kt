package com.malec.ino.service.network

import kotlinx.coroutines.flow.Flow

interface RoomsApi {
	fun getRooms(): Flow<RoomResult>
}