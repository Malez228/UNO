package com.malec.ino.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics

class AndroidKeyGenerator(private val activity: Activity): KeyGenerator {
	override fun generate(): String {
		return getTelephonyData() + "☺" + getDeviceData()
	}

	private fun getTelephonyData(): String {
		val tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
		return tm?.networkOperatorName + "☺" + tm?.simCountryIso
	}

	private fun getDeviceData(): String {
		return Runtime.getRuntime()
			.maxMemory()
			.toString() + "☺" + getDisplayData() + "☺" + Build.MODEL.normalize() + "☺" + Build.VERSION.RELEASE.normalize()
	}

	private fun getDisplayData(): String {
		val dm = DisplayMetrics()
		activity.windowManager.defaultDisplay.getMetrics(dm)
		return dm.widthPixels.toString() + "☺" + dm.heightPixels + "☺" + dm.densityDpi
	}

	private fun String.normalize(): String {
		return this.replace('.', '☻').replace('#', '☻').replace('$', '☻').replace('[', '☻').replace(']', '☻')
	}
}