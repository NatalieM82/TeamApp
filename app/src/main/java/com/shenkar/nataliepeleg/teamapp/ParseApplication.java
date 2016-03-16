package com.shenkar.nataliepeleg.teamapp;

/**
 * Created by NatalieMenahem on 07/03/2016.
 */

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {
    @Override
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
}
