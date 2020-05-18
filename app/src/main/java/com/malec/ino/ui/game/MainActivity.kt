package com.malec.ino.ui.game

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity: AppCompatActivity(), HasAndroidInjector {
	@Inject
	lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	val viewModel: MainViewModel by viewModels {
		viewModelFactory
	}

	override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}
}