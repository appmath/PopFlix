package com.aziz.udacity.popflix;

import android.app.Application;
import android.util.Log;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import timber.log.Timber;

/**
 * Application for flix app.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FlickApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
