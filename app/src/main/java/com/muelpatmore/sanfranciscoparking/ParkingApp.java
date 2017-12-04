package com.muelpatmore.sanfranciscoparking;

import android.app.Application;
import android.content.Context;

import com.muelpatmore.sanfranciscoparking.data.injection.DaggerPresenterComponent;
import com.muelpatmore.sanfranciscoparking.data.injection.PresenterComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        instance = this;
        mContext = this.getApplicationContext();
        presenterComponent = DaggerPresenterComponent.create();

        Realm.init(mContext);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
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
