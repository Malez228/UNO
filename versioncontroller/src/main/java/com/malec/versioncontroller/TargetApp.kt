package com.proj.versioncontroller

class TargetApp(var apkName: String, val apkUrl: String, val historyUrl: String) {
    private var historyLineSeparator = ";"
    private var historyVersionSeparator = " "

    init {
        apkName = if (apkName.toLowerCase().contains(".apk")) apkName else "$apkName.apk"
    }

    var appName = apkName.dropLast(4)

    fun TargetApp.setHistoryLineSeparator(value: String): TargetApp{
        this.historyLineSeparator = value
        return this
    }

    fun TargetApp.setHistoryVersionSeparator(value: String): TargetApp{
        this.historyVersionSeparator = value
        return this
    }
}