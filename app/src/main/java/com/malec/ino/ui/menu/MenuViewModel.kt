package com.malec.ino.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.malec.ino.model.Room
import com.malec.ino.repo.RoomRepo
import com.malec.ino.service.network.RoomResult
import com.malec.ino.ui.menu.fragments.RoomAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MenuViewModel @Inject constructor(private val repo: RoomRepo): ViewModel(), RoomAdapter.EnterRoom {
	val room: LiveData<RoomResult>
		get() = repo.next().asLiveData()

	override fun prepareEnter(room: Room) {

	}

}