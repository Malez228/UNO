package com.malec.ino.service.network

import com.google.firebase.database.*
import com.malec.ino.model.Room
import com.malec.ino.model.RoomD
import com.malec.ino.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@ExperimentalCoroutinesApi
class FirebaseRoomsApi(private val db: DatabaseReference): RoomsApi {
	override fun getRooms(): Flow<RoomResult> {
		return channelFlow {
			val listener = object: ChildEventListener {
				override fun onCancelled(p0: DatabaseError) {}
				override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

				override fun onChildChanged(p0: DataSnapshot, p1: String?) {
					p0.getValue(RoomD::class.java)?.let {
						channel.offer(RoomResult(it.makeRoom(), RoomAction.UPDATE))
					}
				}

				override fun onChildAdded(p0: DataSnapshot, p1: String?) {
					p0.getValue(RoomD::class.java)?.let {
						channel.offer(RoomResult(it.makeRoom(), RoomAction.ADD))
					}
				}

				override fun onChildRemoved(p0: DataSnapshot) {
					p0.getValue(RoomD::class.java)?.let {
						channel.offer(RoomResult(it.makeRoom(), RoomAction.REMOVE))
					}
				}
			}

			db.addChildEventListener(listener)

			awaitClose { db.removeEventListener(listener) }
		}
	}

	override fun removeRoom(room: Room) {
		db.child(room.name).removeValue()
	}

	override fun createRoom(room: Room) {
		db.child(room.name).setValue(room.toMap())
	}

	override fun enterRoom(room: Room, user: User) {
		db.child(room.name).runTransaction(object: Transaction.Handler {
			override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {

			}

			override fun doTransaction(data: MutableData): Transaction.Result {
				data.child("connectedPlayers").value = room.connectedPlayers + 1
				data.child("playersKeys").value = room.playersKeys.joinToString(";") + ";" + user.key

				return Transaction.success(data)
			}
		})
	}
}