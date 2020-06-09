package com.malec.ino.ui.menu.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.malec.ino.di.Injectable
import com.malec.ino.ui.menu.MenuViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewRoomFragment: Fragment(), Injectable {
	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private val viewModel: MenuViewModel by activityViewModels {
		viewModelFactory
	}
}