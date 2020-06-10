package com.malec.ino.ui.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malec.ino.R
import com.malec.ino.di.Injectable
import com.malec.ino.model.User
import com.malec.ino.ui.menu.MenuViewModel
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class StatsFragment: Fragment(), Injectable {
	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private val viewModel: MenuViewModel by activityViewModels {
		viewModelFactory
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_stats, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initViewModelListeners()
		initViewListeners()
	}

	private fun initViewModelListeners() {
		viewModel.user.observe(viewLifecycleOwner, Observer {
			showUserStats(it)
		})

		viewModel.isSaveNameBtnVisible.observe(viewLifecycleOwner, Observer { isVisible ->
			if (isVisible) saveLayout.visibility = View.VISIBLE
			else saveLayout.visibility = View.GONE
		})

		viewModel.nameSaveResult.observe(viewLifecycleOwner, Observer { result ->
			when (result) {
				MenuViewModel.UserNameSaveResult.SUCCESS -> {
					viewModel.nameSaveResult.value = MenuViewModel.UserNameSaveResult.NONE
					Toast.makeText(requireContext(), getString(R.string.StatNameSaved), Toast.LENGTH_SHORT).show()
				}
				MenuViewModel.UserNameSaveResult.FAILURE -> {
					viewModel.nameSaveResult.value = MenuViewModel.UserNameSaveResult.NONE
					userNameLayout.error = getString(R.string.StatNameError)
					userNameEditText.requestFocus()
				}
				else                                     -> {
				}
			}
		})
	}

	private fun initViewListeners() {
		userNameEditText.doAfterTextChanged {
			userNameLayout.error = null

			viewModel.onUserNameChange(it?.toString())
		}

		userNameEditText.setOnEditorActionListener { _, actionId, _ ->
			return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
				viewModel.onSaveNameClick()
				true
			} else false
		}

		saveButton.setOnClickListener {
			viewModel.onSaveNameClick()
		}
	}

	private fun showUserStats(user: User) {
		userNameEditText.setText(user.name)
		gamesText.text = getString(R.string.StatGames, user.games.toString())
		val winRate = (if (user.games > 0) user.wins / user.games.toFloat() else 0f) * 100
		winRateText.text = getString(R.string.StatWinRate, String.format("%.1f", winRate))
		winsText.text = getString(R.string.StatWins, user.wins.toString())
		losesText.text = getString(R.string.StatLoses, user.loses.toString())
	}
}