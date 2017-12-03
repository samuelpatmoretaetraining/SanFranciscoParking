package com.muelpatmore.sanfranciscoparking.data;

import android.content.Context;

import com.muelpatmore.sanfranciscoparking.data.network.APIServiceInterface;

/**
 * Created by Samuel on 03/12/2017.
 */

public interface DataManagerInterface extends APIServiceInterface {
    void sendReservationNotification(String messageBody);
}
