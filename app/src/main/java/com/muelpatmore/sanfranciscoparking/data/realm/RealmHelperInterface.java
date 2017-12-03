package com.muelpatmore.sanfranciscoparking.data.realm;

import android.util.Log;

import com.muelpatmore.sanfranciscoparking.data.realm.realmobjects.RealmReservation;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface RealmHelperInterface {
    void saveReservation(RealmReservation reservation);
    ArrayList<RealmReservation> getReservations();
}
