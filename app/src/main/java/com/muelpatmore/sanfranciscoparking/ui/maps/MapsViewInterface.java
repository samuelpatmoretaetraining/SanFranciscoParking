package com.muelpatmore.sanfranciscoparking.ui.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.ui.base.MvpView;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface MapsViewInterface extends MvpView {
    void plotAndFocusOnUser();

    LatLng getUserLocation();
    void setUserLocation(LatLng userLocation);
    GoogleMap getMap();
}
