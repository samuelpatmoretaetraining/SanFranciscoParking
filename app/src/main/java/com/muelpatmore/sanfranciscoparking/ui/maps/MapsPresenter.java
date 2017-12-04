package com.muelpatmore.sanfranciscoparking.ui.maps;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.muelpatmore.sanfranciscoparking.data.DataManager;
import com.muelpatmore.sanfranciscoparking.data.messages.IndividualParkingSpotReceived;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotReservedConfirmation;
import com.muelpatmore.sanfranciscoparking.data.messages.ParkingSpotsDataReceived;
import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;
import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;
import com.muelpatmore.sanfranciscoparking.ui.utils.DateUtils;

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
    public MapsPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void onStart() {

        EventBus.getDefault().register(this);
    }

    /**
     * Attach MapsView to Presenter to allow two way communication and data transfer.
     * @param view MapsViewInterface object presenter is attached to.
     */
    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    /**
     * OnDetachment from MapsView unregister from EventBus thread lister and clear disposable
     * requests in the DataManager.
     */
    @Override
    public void onDetach() {
        mDataManager.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Request for list of ParkingSpaces surrounding the given location. Confirmation by a
     * ParkingSpotsDataReceived event.
     * @param location point around which to look for parking spaces.
     */
    public void fetchParkingSpacesNear(LatLng location) {
        view.getMap().clear();
        mDataManager.fetchParkingSpots(location);
        view.plotAndFocusOnUser();
    }

    /**
     * Request for full details of ParkingSpace with given id. Confirmation by a
     * IndividualParkingSpotReceived event.
     * @param id id of requested ParkingSpace.
     */
    public void parkingSpaceDetailsRequested(int id) {
        mDataManager.fetchParkingSpotById(id);
    }

    /**
     * Request that ParkingSpace entry with selected id is reserved & set to the status of the
     * given ParkingSpace object. Confirmation by a ParkingSpotReservedConfirmation event.
     * @param id id of ParkingSpace to update.
     * @param parkingSpace model ParkingSpace object to replace current entry.
     */
    public void reserveParkingSpot(int id, ParkingSpaceModel parkingSpace) {
        mDataManager.reserveParkingSpot(id, parkingSpace);
    }

    /**
     * Listener for result of parking spaces near location query that occurs on another thread.
     * @param event Messenger class that reporting result of parking spaces near location query.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParkingSpotsDataReceived event) {
        Log.i(TAG, "ParkingSpotsDataReceived, size: "+event.getPoints().size());
        view.plotMapMarkers(event.getPoints());
    }

    /**
     * Listener for result of individual parking space lookup query that occurs on another thread.
     * @param event Messenger class that reporting result of individual parking space lookup query.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IndividualParkingSpotReceived event) {
        Log.i(TAG, "IndividualParkingSpotsReceived, id: "+event.getParkingSpot().getId());
        view.showParkingSpaceDetails(event.getParkingSpot());
    }

    /**
     * Listener for result of parking space reservation query that occurs on another thread.
     * @param event Messenger class that reporting result of parking space reservation query.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ParkingSpotReservedConfirmation event) {
        if(event.isReservationStatus()) {
            String reservedUntil = event.getReservedUntil();

            reservedUntil = reservedUntil.substring(11,reservedUntil.indexOf("."));
            view.showMessage("Parking space reserved until "+reservedUntil);
            mDataManager.sendReservationNotification("Parking spot reserved till: "+reservedUntil);

            fetchParkingSpacesNear(view.getUserLocation());
            // Store reservation in Realm database
            RealmReservation entry = new RealmReservation(event.getId(), event.getReservedUntil());
            mDataManager.saveReservation(entry);
        } else {
            view.showMessage("Reservation failed, please try again");
        }
    }

}
