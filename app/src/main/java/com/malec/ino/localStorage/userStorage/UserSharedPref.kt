package com.malec.ino.localStorage.userStorage

import android.content.Context
import com.malec.ino.localStorage.clear
import com.malec.ino.localStorage.get
import com.malec.ino.localStorage.set
import com.malec.ino.model.User

class UserSharedPref(context: Context): UserStorage {
	private val localStorage = context.getSharedPreferences(UserPreferences.PREFERENCES.key, Context.MODE_PRIVATE)

	override fun getUser(): User = User(
		getKey(), getName(), getGames(), getWins(), getLoses()
	)

	override fun isUserAuthorized(): Boolean = localStorage.contains(UserPreferences.KEY.key)

	override fun getKey(): String = localStorage[UserPreferences.KEY.key]

	override fun setKey(value: String) {
		localStorage[UserPreferences.KEY.key] = value
	}

	override fun getName(): String = localStorage[UserPreferences.NAME.key]

	override fun setName(value: String) {
		localStorage[UserPreferences.NAME.key] = value
	}

	override fun getGames(): Int = localStorage[UserPreferences.GAMES.key]

	override fun setGames(value: Int) {
		localStorage[UserPreferences.GAMES.key] = value
	}

	override fun getWins(): Int = localStorage[UserPreferences.WINS.key]

	override fun setWins(value: Int) {
		localStorage[UserPreferences.WINS.key] = value
	}

	override fun getLoses(): Int = localStorage[UserPreferences.LOSES.key]

	override fun setLoses(value: Int) {
		localStorage[UserPreferences.LOSES.key] = value
	}

	override fun clear() {
		localStorage.clear()
	}
}