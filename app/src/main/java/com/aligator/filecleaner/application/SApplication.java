package com.aligator.filecleaner.application;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

public class SApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this.getApplicationContext(), "51ab5e8e40", false);
    }
}
