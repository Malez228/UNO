package com.malec.ino.localStorage.userStorage

import com.malec.ino.model.User

interface UserStorage {
	fun getUser(): User

	fun isUserAuthorized(): Boolean

	fun getKey(): String
	fun setKey(value: String)

	fun getName(): String
	fun setName(value: String)

	fun getGames(): Int
	fun setGames(value: Int)

	fun getWins(): Int
	fun setWins(value: Int)

	fun getLoses(): Int
	fun setLoses(value: Int)

	fun clear()
}