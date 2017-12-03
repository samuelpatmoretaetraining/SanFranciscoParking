package com.muelpatmore.sanfranciscoparking.ui.reservations;

import com.muelpatmore.sanfranciscoparking.data.DataManager;
import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;
import com.muelpatmore.sanfranciscoparking.ui.maps.MapsViewInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Samuel on 03/12/2017.
 */

public class ReservationsActivityPresenter implements ReservationsActivityPresenterInterface {

    private static final String TAG = "MapsPresenter";

    private ReservationsActivityViewInterface view;
    private DataManager mDataManager;

    public ReservationsActivityPresenter() {
        mDataManager = new DataManager();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(ReservationsActivityViewInterface mvpView) {
        view = mvpView;
    }

    @Override
    public void requestReservationList() {
        ArrayList<RealmReservation> reservationList =  mDataManager.getReservations();
        view.setReservationList(reservationList);
        view.refreshReservationList();
    }

    @Override
    public void onDetach() {
        mDataManager.onStop();
        //EventBus.getDefault().unregister(this);
    }
}
