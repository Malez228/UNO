package com.malec.ino.service.localDb

import androidx.room.TypeConverter

class Converter {
	@TypeConverter
	fun fromList(list: List<String>) = list.joinToString("♂")

	@TypeConverter
	fun fromString(s: String) = s.split("♂")
}