package com.wanna.client.ui;

import android.app.Application;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.wanna.data.ArrayActivityAll;

import java.util.HashMap;

/**
 * Created by josh on 15/4/15.
 */
public class MainApplication extends Application {

    public static Session session;

    public static GraphUser facebookuser;

    public static HashMap<String, GraphUser> facebookFriends = new HashMap<>();
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    public static ArrayActivityAll lastActivitiesHome;
    public static ArrayActivityAll lastActivitiesSearch;


    @Override
    public void onCreate() {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-63430025-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

}
