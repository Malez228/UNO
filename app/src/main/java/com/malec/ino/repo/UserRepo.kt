package com.malec.ino.repo

import com.malec.ino.localStorage.userStorage.UserStorage
import com.malec.ino.model.User
import com.malec.ino.util.KeyGenerator

class UserRepo(private val userStorage: UserStorage) {
	fun isUserAuthorized() = userStorage.isUserAuthorized()

	fun getUser() = userStorage.getUser()

	fun authorizeUser(keyGenerator: KeyGenerator) {
		val user = User(
			keyGenerator.generate().toString(), generateName(), 0, 0, 0
		)
		saveUser(user)
	}

	fun updateName(newName: String) {
		userStorage.setName(newName)
	}

	private fun generateName() = "User" + (1000 .. 9999).random()

	private fun saveUser(user: User) {
		userStorage.apply {
			setKey(user.key)
			setName(user.name)
			setGames(user.games)
			setWins(user.wins)
			setLoses(user.loses)
		}
	}
}