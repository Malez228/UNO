package com.malec.ino.ui.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malec.ino.R
import com.malec.ino.di.Injectable
import com.malec.ino.ui.menu.MenuViewModel
import kotlinx.android.synthetic.main.fragment_new_room.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewRoomFragment: Fragment(), Injectable {
	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private val viewModel: MenuViewModel by activityViewModels {
		viewModelFactory
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_new_room, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		roomSizeSpinner.setSelection(roomSizeSpinner.count - 1)

		initViewListeners()

		viewModel.roomCreateResult.observe(viewLifecycleOwner, Observer { result ->
			when (result) {
				MenuViewModel.RoomCreateResult.SUCCESS              -> {
					viewModel.roomCreateResult.value = MenuViewModel.RoomCreateResult.NONE
					Toast.makeText(requireContext(), getString(R.string.RoomCreated), Toast.LENGTH_SHORT).show()
				}
				MenuViewModel.RoomCreateResult.ERROR_NAME           -> {
					viewModel.roomCreateResult.value = MenuViewModel.RoomCreateResult.NONE
					nameLayout.error = getString(R.string.StatNameError)
					nameEditText.requestFocus()
				}
				MenuViewModel.RoomCreateResult.ERROR_NAME_SHORT     -> {
					viewModel.roomCreateResult.value = MenuViewModel.RoomCreateResult.NONE
					nameLayout.error = getString(R.string.RoomNameMinSize)
					nameEditText.requestFocus()
				}
				MenuViewModel.RoomCreateResult.ERROR_NAME_DUPLICATE -> {
					viewModel.roomCreateResult.value = MenuViewModel.RoomCreateResult.NONE
					Toast.makeText(requireContext(), getString(R.string.RoomAlive), Toast.LENGTH_SHORT).show()
				}
				MenuViewModel.RoomCreateResult.ERROR_PASS           -> {
					viewModel.roomCreateResult.value = MenuViewModel.RoomCreateResult.NONE
					passLayout.error = getString(R.string.StatNameError)
				}
				else                                                -> {
				}
			}
		})
	}

	private fun initViewListeners() {
		roomSizeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(p0: AdapterView<*>?) {
			}

			override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
				viewModel.onRoomSizeChange(position + 1)
			}
		}

		nameEditText.doAfterTextChanged {
			nameLayout.error = null

			viewModel.onRoomNameChange(it?.toString())
		}

		passEditText.doAfterTextChanged {
			passLayout.error = null

			viewModel.onRoomPassChange(it?.toString())
		}

		createFAB.setOnClickListener {
			viewModel.onCreateRoomClick()
		}
	}
}