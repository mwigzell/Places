package com.mwigzell.places.util;

import android.content.Context;

import com.mwigzell.places.BuildConfig;
import com.mwigzell.places.R;

import java.util.Locale;

import timber.log.Timber;


/**
 * Created by mwigzell on 12/10/16.
 */

public class Log {
    private static final int PREFACE_LENGTH = 50;
    private static final String SPACE_STR = "                                                                                  ";

    private static String app;

    public static void init(Context context) {
        app = context.getString(R.string.app_name);
        Timber.plant(new CustomTree());
    }

    private static String getLogPreface() {
        String str;
        int stackIndex = 9;
        StackTraceElement[] stackElement = Thread.currentThread().getStackTrace();
        String className = stackElement[stackIndex].getClassName();
        int index = className.lastIndexOf('.');
        long now = System.currentTimeMillis();
        if (index != -1)
            className = className.substring(index + 1);
        str = String.format(Locale.US, "%s: %s.%s:%d", app, className, stackElement[stackIndex].getMethodName(), stackElement[stackIndex].getLineNumber());
        if (str.length() < PREFACE_LENGTH)
            str += SPACE_STR.substring(0, PREFACE_LENGTH - str.length());

        return str;
    }

    private static class CustomTree extends Timber.DebugTree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if(!BuildConfig.DEBUG && priority != android.util.Log.ERROR) {
                return;
            }

            tag = getLogPreface();
            super.log(priority, tag, message, t);
        }
    }
}
