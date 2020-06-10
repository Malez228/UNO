package com.malec.ino.model

data class Room(val name: String, val pass: String, val connectedPlayers: Int, val maxPlayers: Int, val turns: Int, val time: Int,
                val playersKeys: List<String>) {
	fun toMap(): HashMap<String, *> {
		return hashMapOf(
			"name" to name, "pass" to pass, "connectedPlayers" to connectedPlayers, "maxPlayers" to maxPlayers, "turns" to turns, "time" to time, "playersKeys" to playersKeys.joinToString(";")
		)
	}
}

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