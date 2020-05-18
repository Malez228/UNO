package com.malec.ino.localStorage.userStorage

enum class UserPreferences(val key: String) {
	PREFERENCES("UserData"),

	KEY("PhoneKey"), NAME("UserName"), GAMES("UserGames"), WINS("UserWins"), LOSES("UserLoses")
}