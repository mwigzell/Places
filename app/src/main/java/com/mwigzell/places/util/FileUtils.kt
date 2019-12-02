package com.mwigzell.places.util

import java.io.File
import javax.inject.Inject

class FileUtils @Inject constructor() {

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
