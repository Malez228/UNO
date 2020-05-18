package com.proj.versioncontroller

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import java.io.File
import java.net.URL

class VersionController(private val context: Context, private val targetApp: TargetApp) {
    private class Version(val name: String, val changes: String)

    private inner class DownloadController
    {
        fun enqueue(): Uri
        {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(targetApp.apkUrl)
            val request = DownloadManager.Request(uri)
            request.apply {
                setTitle(targetApp.appName)
                setDescription(context.getString(R.string.download_description))
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                setVisibleInDownloadsUi(true)
            }
            val fileName = Uri.fromFile(File(context.getExternalFilesDir(null), targetApp.apkName))
            request.setDestinationUri(fileName)
            downloadManager.enqueue(request)

            return fileName
        }
    }

    private inner class FileController
    {
        fun enqueue(fileName: Uri)
        {
            val downloadIntent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val newFile = File(context.getExternalFilesDir(null), targetApp.apkName)
                val providerFileName = FileProvider.getUriForFile(context, context.packageName, newFile)

                downloadIntent = Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
                    data = providerFileName
                    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            } else {
                downloadIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = fileName
                    setDataAndType(fileName, "application/vnd.android.package-archive")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }

            context.registerReceiver(object: BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {
                    context.startActivity(downloadIntent)
                    context.unregisterReceiver(this)
                }
            }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
    }

    private fun fetchVersionData(): String = URL(targetApp.historyUrl).readText()

    private fun appVersionName(): String = context.packageManager.getPackageInfo(context.packageName, 0).versionName

    fun checkVersion() = isNewVersion()?.let { showDialog(it) }

    private fun showDialog(version: Version)
    {
        val translatedMessage = context.getString(R.string.dialog_message)
        val message = translatedMessage + "\n\n" + version.changes
        val alert = AlertDialog.Builder(context)
        alert.apply {
            setTitle(context.getString(R.string.dialog_title))
            setMessage(message)
            setPositiveButton(context.getString(R.string.dialog_update)) { _, _ ->
                //Удаляем старые версии
                context.getExternalFilesDir(null)?.listFiles()?.let { files ->
                    files.forEach {
                        if (it.name.contains(targetApp.apkName))
                            it.delete()
                    }
                }

                //Скачивание файла
                val fileName = DownloadController().enqueue()

                //Запись Файла и установка
                FileController().enqueue(fileName)
            }
            setNegativeButton(context.getString(R.string.dialog_cancel)){dialog, _ -> dialog.cancel()}

        }.show()
    }

    private fun getNewVersion(): Version {
        val job = GlobalScope.async { fetchVersionData() }
        val versionString = runBlocking { job.await() }

        val prodApkVersion = versionString.split(" ")[0];
        var prodApkChanges = versionString.split("$prodApkVersion ")[1]

        prodApkChanges = prodApkChanges.substringBefore("\n")
        val translatedChanges = context.getString(R.string.dialog_changes)
        prodApkChanges = translatedChanges + " " + prodApkVersion + ": \n" + prodApkChanges.split(";").joinToString("\n") { it[0].toUpperCase() + it.substring(1) }

        return Version(prodApkVersion, prodApkChanges)
    }

    private fun isNewVersion(): Version? = getNewVersion().takeIf { it.name != appVersionName()}
}