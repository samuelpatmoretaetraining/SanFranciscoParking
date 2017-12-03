package com.muelpatmore.sanfranciscoparking.ui.maps;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.data.DataManager;

/**
 * Created by Samuel on 03/12/2017.
 */

public class MapsPresenter<V extends MapsViewInterface> implements MapsPresenterInterface<V> {

    private static final String TAG = "MapsPresenter";

    private MapsViewInterface view;
    private DataManager mDataManager;


    public MapsPresenter() {
        mDataManager = new DataManager();
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {

    }


    public void fetchParkingSpacesNear(LatLng location) {
        view.getMap().clear();
        mDataManager.fetchParkingSpots(location);
        view.plotAndFocusOnUser();
    }
}
