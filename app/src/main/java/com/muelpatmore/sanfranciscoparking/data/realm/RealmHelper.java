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
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("parking.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void saveReservation(RealmReservation reservation) {
        realm.executeTransaction(realm1 -> realm.copyToRealm(reservation));
    }

    @Override
    public ArrayList<RealmReservation> getReservations() {
        ArrayList<RealmReservation> reservations = new ArrayList<>();

        RealmResults<RealmReservation> realmData = realm.where(RealmReservation.class).findAll();
        for(RealmReservation r : realmData){
            reservations.add(new RealmReservation(r.getId()));
        }

        return reservations;
    }

}
