package com.malec.ino.di.activity

import com.malec.ino.ui.menu.fragments.NewRoomFragment
import com.malec.ino.ui.menu.fragments.RoomsFragment
import com.malec.ino.ui.menu.fragments.StatsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class FragmentBuildersModule {
	@ContributesAndroidInjector
	abstract fun contributeNewRoomFragment(): NewRoomFragment

	@ContributesAndroidInjector
	abstract fun contributeRoomsFragment(): RoomsFragment

	@ContributesAndroidInjector
	abstract fun contributeStatsFragment(): StatsFragment
}