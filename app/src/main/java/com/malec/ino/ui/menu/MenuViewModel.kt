package com.malec.ino.ui.menu

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.ino.model.Room
import com.malec.ino.model.User
import com.malec.ino.repo.RoomRepo
import com.malec.ino.repo.UserRepo
import com.malec.ino.service.network.RoomAction
import com.malec.ino.ui.menu.fragments.RoomAdapter
import com.malec.ino.util.AndroidKeyGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MenuViewModel @Inject constructor(private val roomRepo: RoomRepo,
                                        private val userRepo: UserRepo): ViewModel(), RoomAdapter.EnterRoom {
	private val list = mutableListOf<Room>()

	private var userName: String? = null
	private var userNameChanged: String? = null

	private var roomName: String = ""
	private var roomPass: String = ""
	private var roomSize: Int = 1

	private val _rooms = MutableLiveData<List<Room>>()
	val rooms: LiveData<List<Room>>
		get() = _rooms

	private val _user = MutableLiveData<User>()
	val user: LiveData<User>
		get() = _user

	private val _isSaveNameBtnVisible = MutableLiveData<Boolean>()
	val isSaveNameBtnVisible: LiveData<Boolean>
		get() = _isSaveNameBtnVisible

	val nameSaveResult = MutableLiveData<UserNameSaveResult>()
	val roomCreateResult = MutableLiveData<RoomCreateResult>()

	override fun prepareEnter(room: Room) {

	}

	init {
		updateRooms()
	}

	fun onRoomNameChange(newName: String?) {
		newName?.let {
			roomName = it.trim()
		}
	}

	fun onRoomPassChange(newPass: String?) {
		newPass?.let {
			roomPass = it.trim()
		}
	}

	fun onRoomSizeChange(newSize: Int?) {
		newSize?.let {
			roomSize = it
		}
	}

	fun onCreateRoomClick() {
		val isValid = isRoomValid()
		roomCreateResult.value = isValid

		if (isValid == RoomCreateResult.SUCCESS) roomRepo.create(roomName, roomPass, roomSize)
	}

	private fun isRoomValid(): RoomCreateResult {
		val re = Regex("^[А-Яа-яA-Za-z0-9 _-]*$")

		return when {
			!roomName.matches(re)                         -> RoomCreateResult.ERROR_NAME
			!(roomName.length >= 3 || roomName.isEmpty()) -> RoomCreateResult.ERROR_NAME_SHORT
			!list.none { it.name == roomName }            -> RoomCreateResult.ERROR_NAME_DUPLICATE
			!roomPass.matches(re)                         -> RoomCreateResult.ERROR_PASS

			else                                          -> RoomCreateResult.SUCCESS
		}
	}

	private fun updateRooms() {
		viewModelScope.launch(Dispatchers.IO) {
			roomRepo.next().collect { result ->
				when (result.action) {
					RoomAction.ADD    -> list.add(result.room)
					RoomAction.UPDATE -> {
						val i = list.indexOfFirst { it.name == result.room.name }
						list[i] = result.room
					}
					RoomAction.REMOVE -> list.removeAll { it.name == result.room.name }
				}
				_rooms.postValue(list)
			}
		}
	}

	fun initUser(activity: Activity) {
		if (!userRepo.isUserAuthorized()) userRepo.authorizeUser(AndroidKeyGenerator(activity))

		_user.value = userRepo.getUser()
		userName = _user.value?.name
	}

	fun onUserNameChange(newName: String?) {
		val name = newName?.trim()

		_isSaveNameBtnVisible.value = name != userName && !name.isNullOrBlank()
		userNameChanged = name
	}

	fun onSaveNameClick() {
		userNameChanged?.let {
			val re = Regex("^[А-Яа-яA-Za-z0-9 _-]*$")
			nameSaveResult.value = if (it.length > 4 && it.matches(re)) {
				userRepo.updateName(it)
				userName = it
				onUserNameChange(it)
				UserNameSaveResult.SUCCESS
			} else UserNameSaveResult.FAILURE
		}
	}

	enum class RoomCreateResult {
		SUCCESS, ERROR_NAME, ERROR_NAME_SHORT, ERROR_NAME_DUPLICATE, ERROR_PASS, NONE
	}

	enum class UserNameSaveResult {
		SUCCESS, FAILURE, NONE
	}
}