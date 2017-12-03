package com.muelpatmore.sanfranciscoparking;

import android.app.Application;
import android.content.Context;

/**
 * Created by Samuel on 02/12/2017.
 */

public class ParkingApp extends Application {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }
}
