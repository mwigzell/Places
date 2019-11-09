package com.mwigzell.places.util

import android.content.Context
import android.os.Environment

import java.io.File
import java.io.IOException

import javax.inject.Inject

import timber.log.Timber


/**
 * Created by mwigzell on 10/30/15.
 */
class FileUtils @Inject
constructor(private val context: Context) {

    val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == state) {
                true
            } else false
        }

    /* Checks if external storage is available to at least read */
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    val dynamicPreloadFilesDir: File
        get() {
            var path: File?
            if (isExternalStorageReadable && isExternalStorageWritable) {
                path = context!!.getExternalFilesDir(null)
                if (path == null || !path.exists()) {
                    path = context!!.filesDir
                }
            } else
                path = context!!.filesDir
            if (DEBUG) {
                try {
                    Timber.d("Storing files in: " + path!!.canonicalPath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return path
        }

    private fun privateChmodDirStructure(file: File?) {
        if (file == null)
            return
        privateChmodDirStructure(file.parentFile)
        file.setExecutable(true, false)
    }

    fun chmodDirStructure(file: File?) {
        if (file != null) {
            file.setReadable(true, false)
            privateChmodDirStructure(file)
        }
    }

    fun removeDir(dir: File) {
        if (dir.exists()) {
            val files = dir.listFiles()
            if (files != null) {
                for (i in files.indices) {
                    if (files[i].isDirectory) {
                        removeDir(files[i])
                    } else {
                        files[i].delete()
                    }
                }
            }
            dir.delete()
        }
    }

    companion object {
        private val DEBUG = false
    }
}
