package com.malec.ino.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.ino.model.Room
import com.malec.ino.repo.RoomRepo
import com.malec.ino.service.network.RoomAction
import com.malec.ino.ui.menu.fragments.RoomAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MenuViewModel @Inject constructor(private val repo: RoomRepo): ViewModel(), RoomAdapter.EnterRoom {
	private val list = mutableListOf<Room>()

	private val _rooms = MutableLiveData<List<Room>>()
	val rooms: LiveData<List<Room>>
		get() = _rooms

	override fun prepareEnter(room: Room) {

	}

	init {
		viewModelScope.launch(Dispatchers.IO) {
			repo.next().collect {result ->
				when (result.action) {
					RoomAction.ADD    -> list.add(result.room)
					RoomAction.UPDATE -> {
						val i = list.indexOfFirst {it.name == result.room.name}
						list[i] = result.room
					}
					RoomAction.REMOVE -> list.removeAll {it.name == result.room.name}
				}
				_rooms.postValue(list)
			}
		}
	}
}