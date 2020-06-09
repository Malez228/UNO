package com.malec.ino.repo

import com.malec.ino.service.network.RoomsApi

class RoomRepo(private val api: RoomsApi) {
	fun next() = api.getRooms()
}