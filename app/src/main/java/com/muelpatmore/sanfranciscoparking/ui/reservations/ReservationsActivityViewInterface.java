package com.muelpatmore.sanfranciscoparking.ui.reservations;

import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;
import com.muelpatmore.sanfranciscoparking.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface ReservationsActivityViewInterface
        extends MvpView {

    void setReservationList(ArrayList<RealmReservation> reservationList);
    void refreshReservationList();
}
