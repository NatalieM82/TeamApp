package com.shenkar.nataliepeleg.teamapp;

/**
 * Created by NatalieMenahem on 07/03/2016.
 */

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

    private Tracker mTracker;

    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "dMn9uYbGqrb0bdCBfA457Lqaon9Jlv82IRqY8PLX", "7ZtpUG71MSJM2uKwzlUD87irIVtOYZTm1Pmj7cQA");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
