package com.muelpatmore.sanfranciscoparking;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingSpaceModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Samuel on 01/12/2017.
 */

public interface APIServiceInterface {
    void fetchParkingSpots(LatLng location);
    void fetchParkingSpotById(int id);
    void reserveParkingSpot(int id, ParkingSpaceModel parkingSpaceModel);
}
