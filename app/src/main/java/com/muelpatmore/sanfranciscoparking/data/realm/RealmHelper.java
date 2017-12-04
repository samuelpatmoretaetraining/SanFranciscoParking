package com.muelpatmore.sanfranciscoparking.data.realm;

import android.content.Context;
import android.util.Log;

import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Samuel on 03/12/2017.
 */

public class RealmHelper implements RealmHelperInterface{
    private Realm realm;

    public RealmHelper(Context context) {
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Store a single RealmReservation object in the Realm database.
     * Stored values are not checked in anyway for duplicity as identical entries are allowed.
     */
    @Override
    public void saveReservation(RealmReservation reservation) {
        realm.executeTransaction(realm1 -> realm.copyToRealm(reservation));
    }

    /**
     * Retrieve all RealmReservation objects currently in the database.
     */
    @Override
    public ArrayList<RealmReservation> getReservations() {
        ArrayList<RealmReservation> reservations = new ArrayList<>();
        RealmResults<RealmReservation> realmData = realm.where(RealmReservation.class).findAll();
        for (RealmReservation r : realmData) {
            reservations.add(new RealmReservation(r.getId()));
        }
        return reservations;
    }

}
