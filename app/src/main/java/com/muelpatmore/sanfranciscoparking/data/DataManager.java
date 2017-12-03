package com.muelpatmore.sanfranciscoparking.data;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.ParkingApp;
import com.muelpatmore.sanfranciscoparking.data.firebase.MyFirebaseMessagingService;
import com.muelpatmore.sanfranciscoparking.data.network.APIService;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;
import com.muelpatmore.sanfranciscoparking.data.prefs.PreferencesHelper;
import com.muelpatmore.sanfranciscoparking.data.prefs.PreferencesHelperInterface;

/**
 * Created by Samuel on 01/12/2017.
 */

public class DataManager implements DataManagerInterface, PreferencesHelperInterface {

    private final APIService mAPIService;
    private final PreferencesHelperInterface mPreferencesHelper;
    private final MyFirebaseMessagingService mMyFirebaseMessagingService;

    public DataManager() {
        mAPIService = new APIService();
        mPreferencesHelper = new PreferencesHelper(ParkingApp.getContext());
        mMyFirebaseMessagingService = new MyFirebaseMessagingService();
    }

    @Override
    public void fetchParkingSpots(LatLng location) {
        mAPIService.fetchParkingSpots(location);
    }

    public void fetchParkingSpotById(int id) {
        mAPIService.fetchParkingSpotById(id);
    }

    @Override
    public void reserveParkingSpot(int id, ParkingSpaceModel parkingSpaceModel) {
        mAPIService.reserveParkingSpot(id, parkingSpaceModel);
    }

    public void onStop() {
        mAPIService.clearRequests();
    }

    @Override
    public void sendReservationNotification(String messageBody) {
        mMyFirebaseMessagingService.sendReservationNotification(messageBody);
    }

    @Override
    public void setUsername(String username) {
        mPreferencesHelper.setUsername(username);
    }

    @Override
    public String getUsername() {
        return mPreferencesHelper.getUsername();
    }

    @Override
    public void setDefaultLocation(LatLng location) {
        mPreferencesHelper.setDefaultLocation(location);
    }

    @Override
    public LatLng getLocation() {
        return mPreferencesHelper.getLocation();
    }
}
