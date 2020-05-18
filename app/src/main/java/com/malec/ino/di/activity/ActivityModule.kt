package com.malec.ino.di.activity

import com.malec.ino.ui.game.MainActivity
import com.malec.ino.ui.menu.MenuActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class ActivityModule {
	@ContributesAndroidInjector
	abstract fun contributeMainActivity(): MainActivity

	@ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
	abstract fun contributeMenuActivity(): MenuActivity
}