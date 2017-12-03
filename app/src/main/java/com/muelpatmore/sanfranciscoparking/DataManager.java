package com.muelpatmore.sanfranciscoparking;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.networkmodels.ParkingSpaceModel;

/**
 * Created by Samuel on 01/12/2017.
 */

public class DataManager implements APIServiceInterface {

    private final APIService mAPIService;

    public DataManager() {
        mAPIService = new APIService();
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
}
