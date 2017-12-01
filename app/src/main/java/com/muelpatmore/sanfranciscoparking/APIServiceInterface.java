package com.muelpatmore.sanfranciscoparking;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Samuel on 01/12/2017.
 */

public interface APIServiceInterface {
    void fetchParkingSpots(LatLng location);
}
