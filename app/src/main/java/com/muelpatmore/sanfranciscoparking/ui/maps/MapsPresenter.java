package com.muelpatmore.sanfranciscoparking.ui.maps;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.data.DataManager;
import com.muelpatmore.sanfranciscoparking.data.messages.IndividualParkingSpotReceived;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotReservedConfirmation;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotsDataReceived;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Samuel on 03/12/2017.
 */

public class MapsPresenter<V extends MapsViewInterface> implements MapsPresenterInterface<V> {

    private static final String TAG = "MapsPresenter";

    private MapsViewInterface view;
    private DataManager mDataManager;

    /**
     * Constructor for MapsPresenter class.
     */
    public MapsPresenter() {
        mDataManager = new DataManager();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        mDataManager.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Calls API for parking spaces around current user location.
     * @param location point around which to look for parking spaces.
     */
    public void fetchParkingSpacesNear(LatLng location) {
        view.getMap().clear();
        mDataManager.fetchParkingSpots(location);
        view.plotAndFocusOnUser();
    }

    public void parkingSpaceDetailsRequested(int id) {
        mDataManager.fetchParkingSpotById(id);
    }

    public void reserveParkingSpot(int id, ParkingSpaceModel parkingSpace) {
        mDataManager.reserveParkingSpot(id, parkingSpace);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParkingSpotsDataReceived event) {
        Log.i(TAG, "ParkingSpotsDataReceived, size: "+event.getPoints().size());
        view.plotMapMarkers(event.getPoints());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IndividualParkingSpotReceived event) {
        Log.i(TAG, "IndividualParkingSpotsReceived, id: "+event.getParkingSpot().getId());
        view.showParkingSpaceDetails(event.getParkingSpot());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParkingSpotReservedConfirmation event) {
        if(event.isReservationStatus()) {
            String reservedUntil = event.getReservedUntil();

            reservedUntil = reservedUntil.substring(11,reservedUntil.indexOf("."));
            view.showMessage("Parking space reserved until "+reservedUntil);
            fetchParkingSpacesNear(view.getUserLocation());
        } else {
            view.showMessage("Reservation failed, please try again");
        }
    }

}
