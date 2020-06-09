package com.malec.ino.service.network

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.malec.ino.model.RoomD
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

			awaitClose {db.removeEventListener(listener)}
		}
	}
}