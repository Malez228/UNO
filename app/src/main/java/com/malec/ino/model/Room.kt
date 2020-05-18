package com.malec.ino.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.malec.ino.service.localDb.Converter

@Entity
@TypeConverters(Converter::class)
data class Room(@PrimaryKey val name: String, val pass: String, val connectedPlayers: Int, val maxPlayers: Int, val turns: Int, val time: Int, val playersKeys: List<String>)