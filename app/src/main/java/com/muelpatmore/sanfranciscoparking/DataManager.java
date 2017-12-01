package com.muelpatmore.sanfranciscoparking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Samuel on 01/12/2017.
 */

public class DataManager implements APIServiceInterface {

    private APIService mAPIService;

    public DataManager() {
        mAPIService = new APIService();
    }

    @Override
    public void fetchParkingSpots(LatLng location) {
        mAPIService.fetchParkingSpots(location);
    }

    public void onStop() {
        mAPIService.clearRequests();
    }
}
