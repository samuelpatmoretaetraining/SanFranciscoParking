package com.muelpatmore.sanfranciscoparking;

import android.app.Application;
import android.content.Context;

import com.muelpatmore.sanfranciscoparking.data.injection.DaggerPresenterComponent;
import com.muelpatmore.sanfranciscoparking.data.injection.PresenterComponent;

/**
 * Created by Samuel on 02/12/2017.
 */

public class ParkingApp extends Application {

    private static Context mContext;
    private PresenterComponent presenterComponent;
    private static ParkingApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        presenterComponent = DaggerPresenterComponent.create();
        instance = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static ParkingApp getInstance() {
        return instance;
    }

    public PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }

    public void setPresenterComponent(PresenterComponent presenterComponent) {
        this.presenterComponent = presenterComponent;
    }
}
