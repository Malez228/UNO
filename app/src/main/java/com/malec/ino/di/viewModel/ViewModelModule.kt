package com.malec.ino.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malec.ino.di.module.NetworkModule
import com.malec.ino.di.module.UserStorageModule
import com.malec.ino.ui.game.MainViewModel
import com.malec.ino.ui.menu.MenuViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [UserStorageModule::class, NetworkModule::class])
abstract class ViewModelModule {
	@Binds
	@IntoMap
	@ViewModelKey(MainViewModel::class)
	abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(MenuViewModel::class)
	abstract fun menuViewModel(viewModel: MenuViewModel): ViewModel

	@Binds
	abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}