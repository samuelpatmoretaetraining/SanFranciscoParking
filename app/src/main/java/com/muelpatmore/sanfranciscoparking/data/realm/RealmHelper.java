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

    private static final String TAG = "RealmHelper";

    private Realm mRealm;

    public RealmHelper(Context context) {
        Realm.init(context);
    }

    public void saveReservation(RealmReservation reservation) {
        try(Realm realmInstance = Realm.getDefaultInstance()) {
            realmInstance.executeTransaction((realm) -> realm.insertOrUpdate(reservation));
        }
    }

    public ArrayList<RealmReservation> getReservations() {
        try(Realm realmInstance = Realm.getDefaultInstance()) {
                ArrayList<RealmReservation> reservations = new ArrayList<RealmReservation>();

            RealmResults<RealmReservation> realmReservations =
                    realmInstance.where(RealmReservation.class).findAll();

            // transfer items from realmResults to customers
            for(RealmReservation realmReservation : realmReservations) {
                reservations.add(realmReservation);
                Log.i(TAG, ""+realmReservation.getId());
            }

            return reservations;
        }
    }

}
