package com.malec.ino.ui.menu.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malec.ino.databinding.ItemRoomBinding
import com.malec.ino.model.Room

class RoomAdapter(private val vm: EnterRoom): ListAdapter<Room, RoomAdapter.RoomItemViewHolder>(diffUtilCallback) {
	companion object {
		private val diffUtilCallback = object: DiffUtil.ItemCallback<Room>() {
			override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
				return oldItem.name == newItem.name
			}

			override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
				return oldItem.name == newItem.name && oldItem.pass == newItem.pass && oldItem.connectedPlayers == newItem.connectedPlayers && oldItem.maxPlayers == newItem.maxPlayers && oldItem.turns == newItem.turns && oldItem.time == newItem.time && oldItem.playersKeys == newItem.playersKeys
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomItemViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding: ItemRoomBinding = ItemRoomBinding.inflate(inflater, parent, false)
		return RoomItemViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
		val room = getItem(position)

		holder.binding?.room = room
	}

	inner class RoomItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
		val binding: ItemRoomBinding? = DataBindingUtil.bind(view)

		init {
			binding?.mainLayout?.setOnClickListener { v ->
				binding.room?.let {
					vm.prepareEnter(it)
				}
			}
		}
	}

	interface EnterRoom {
		fun prepareEnter(room: Room)
	}
}