package com.muelpatmore.sanfranciscoparking.ui.maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.PointModel;
import com.muelpatmore.sanfranciscoparking.ui.base.MvpView;

import java.util.List;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface MapsViewInterface extends MvpView {
    void plotAndFocusOnUser();
    void showParkingSpaceDetails(ParkingSpaceModel parkingSpace);
    void plotMapMarkers(List<PointModel> pointList);

    LatLng getUserLocation();
    void setUserLocation(LatLng userLocation);
    GoogleMap getMap();
}
