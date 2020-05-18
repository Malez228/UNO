package com.malec.ino.repo

import com.malec.ino.service.localDb.RoomsDao

class RoomRepo(private val dao: RoomsDao) {
	fun getAll() = dao.getAll()
}