package com.malec.ino.model

data class Room(val name: String, val pass: String, val connectedPlayers: Int, val maxPlayers: Int, val turns: Int, val time: Int, val playersKeys: List<String>)

class RoomD {
	val name: String = ""
	val pass: String = ""
	val connectedPlayers: Int = 0
	val maxPlayers: Int = 0
	val turns: Int = 0
	val time: Int = 0
	val playersKeys: String = ""

	fun makeRoom() = Room(
		name, pass, connectedPlayers, maxPlayers, turns, time, playersKeys.split(";")
	)
}