package com.muelpatmore.sanfranciscoparking.ui.reservations;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.muelpatmore.sanfranciscoparking.R;
import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;
import com.muelpatmore.sanfranciscoparking.ui.maps.MapsPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReservationsActivityView extends Activity implements ReservationsActivityViewInterface {

    private final static String TAG = "ReservationsActivityView";

    private ReservationsActivityPresenterInterface mReservationsPresenter;
    private ReservationsRecyclerAdapter mReservationsAdapter;
    private ArrayList<RealmReservation> reservationList;

    @BindView(R.id.rcReservations) RecyclerView rcReservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        reservationList = new ArrayList<>();
        mReservationsPresenter = new ReservationsActivityPresenter();
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mReservationsPresenter.onAttach(this);
        mReservationsPresenter.requestReservationList();
    }

    @Override
    public void setReservationList(ArrayList<RealmReservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public void refreshReservationList() {
        if (reservationList.size() < 1) {
            return;
        }
        rcReservations.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mReservationsAdapter = new ReservationsRecyclerAdapter(reservationList, 0, this);
        rcReservations.setAdapter(mReservationsAdapter);
        mReservationsAdapter.notifyDataSetChanged();
//        rcReservations.setAdapter(new ReservationsRecyclerAdapter(
//                reservationList,
//                R.layout.reservation_card,
//                getApplicationContext()));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void hideKeyboard() {

    }
}
