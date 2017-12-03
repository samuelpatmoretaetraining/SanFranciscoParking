package com.muelpatmore.sanfranciscoparking.ui.maps;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.ui.base.MvpPresenter;
import com.muelpatmore.sanfranciscoparking.ui.base.MvpView;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface MapsPresenterInterface<V extends MvpView> extends MvpPresenter <V>{
    void fetchParkingSpacesNear(LatLng location);
    void parkingSpaceDetailsRequested(int id);
}
