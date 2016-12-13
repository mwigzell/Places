package com.mwigzell.places.util;

import android.content.Context;
import android.os.Environment;

import com.mwigzell.places.Application;
import com.mwigzell.places.dagger.Injection;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * Created by mwigzell on 10/30/15.
 */
public class FileUtils {
    private static final boolean DEBUG = false;

    @Inject
    Context context;

    @Inject
    public FileUtils() {
        Injection.instance().getComponent().inject(this);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getDynamicPreloadFilesDir() {
        File path;
        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            path = context.getExternalFilesDir(null);
            if(path == null || !path.exists()){
                path = context.getFilesDir();
            }
        } else
            path = context.getFilesDir();
        if(DEBUG) {
            try {
                Timber.d("Storing files in: " + path.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    private void privateChmodDirStructure(File file){
        if(file == null)
            return;
        privateChmodDirStructure(file.getParentFile());
        file.setExecutable(true, false);
    }

    public void chmodDirStructure(File file){
        if(file != null) {
            file.setReadable(true, false);
            privateChmodDirStructure(file);
        }
    }

    public void removeDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        removeDir(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
            dir.delete();
        }
    }
}
