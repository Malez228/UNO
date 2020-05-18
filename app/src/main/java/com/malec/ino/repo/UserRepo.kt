package com.malec.ino.repo

import com.malec.ino.localStorage.userStorage.UserStorage
import com.malec.ino.model.User

class UserRepo(private val userStorage: UserStorage) {
	fun isUserAuthorized() = userStorage.isUserAuthorized()

	fun getUser() = userStorage.getUser()

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