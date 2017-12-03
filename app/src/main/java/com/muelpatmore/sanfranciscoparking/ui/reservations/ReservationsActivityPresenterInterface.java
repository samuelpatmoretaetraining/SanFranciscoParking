package com.muelpatmore.sanfranciscoparking.ui.reservations;

import com.muelpatmore.sanfranciscoparking.ui.base.MvpPresenter;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface ReservationsActivityPresenterInterface
        extends MvpPresenter<ReservationsActivityViewInterface> {


    void requestReservationList();
}
