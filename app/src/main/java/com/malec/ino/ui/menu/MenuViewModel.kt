package com.malec.ino.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.malec.ino.model.Room
import com.malec.ino.repo.RoomRepo
import com.malec.ino.ui.menu.fragments.RoomAdapter
import javax.inject.Inject

class MenuViewModel @Inject constructor(private val repo: RoomRepo): ViewModel(), RoomAdapter.EnterRoom {
	val rooms: LiveData<List<Room>> = repo.getAll().asLiveData()

	override fun prepareEnter(room: Room) {

	}
}