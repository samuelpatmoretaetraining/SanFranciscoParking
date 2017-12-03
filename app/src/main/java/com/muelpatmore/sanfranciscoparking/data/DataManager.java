package com.muelpatmore.sanfranciscoparking.data;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.ParkingApp;
import com.muelpatmore.sanfranciscoparking.data.firebase.MyFirebaseMessagingService;
import com.muelpatmore.sanfranciscoparking.data.network.APIService;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;
import com.muelpatmore.sanfranciscoparking.data.prefs.PreferencesHelper;
import com.muelpatmore.sanfranciscoparking.data.prefs.PreferencesHelperInterface;
import com.muelpatmore.sanfranciscoparking.data.realm.RealmHelper;
import com.muelpatmore.sanfranciscoparking.data.realm.RealmHelperInterface;
import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;

import java.util.ArrayList;

/**
 * Created by Samuel on 01/12/2017.
 */

public class DataManager implements DataManagerInterface {

    private final APIService mAPIService;
    private final PreferencesHelperInterface mPreferencesHelper;
    private final RealmHelperInterface mRealmHelper;
    private final MyFirebaseMessagingService mMyFirebaseMessagingService;

    public DataManager() {
        mAPIService = new APIService();
        mPreferencesHelper = new PreferencesHelper(ParkingApp.getContext());
        mRealmHelper = new RealmHelper(ParkingApp.getContext());
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

    @Override
    public void saveReservation(RealmReservation reservation) {
        mRealmHelper.saveReservation(reservation);
    }

    @Override
    public ArrayList<RealmReservation> getReservations() {
        return mRealmHelper.getReservations();
    }
}
