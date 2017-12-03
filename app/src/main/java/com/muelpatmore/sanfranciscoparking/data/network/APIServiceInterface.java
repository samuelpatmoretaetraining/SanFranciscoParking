package com.muelpatmore.sanfranciscoparking.data.network;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;

/**
 * Created by Samuel on 01/12/2017.
 */

public interface APIServiceInterface {
    void fetchParkingSpots(LatLng location);
    void fetchParkingSpotById(int id);
    void reserveParkingSpot(int id, ParkingSpaceModel parkingSpaceModel);
}
