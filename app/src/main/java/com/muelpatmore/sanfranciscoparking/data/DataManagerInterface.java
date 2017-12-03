package com.muelpatmore.sanfranciscoparking.data;

import android.content.Context;

import com.muelpatmore.sanfranciscoparking.data.network.APIServiceInterface;
import com.muelpatmore.sanfranciscoparking.data.prefs.PreferencesHelperInterface;
import com.muelpatmore.sanfranciscoparking.data.realm.RealmHelperInterface;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface DataManagerInterface
        extends
            APIServiceInterface,
            PreferencesHelperInterface,
            RealmHelperInterface {
    void sendReservationNotification(String messageBody);
}
