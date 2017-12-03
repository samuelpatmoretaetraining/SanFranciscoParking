package com.muelpatmore.sanfranciscoparking.messages;

import com.muelpatmore.sanfranciscoparking.NetworkModels.ParkingListModel;

/**
 * Created by Samuel on 02/12/2017.
 */

public class IndividualParkingSpotReceived {
    private ParkingListModel parkingSpot;

    public IndividualParkingSpotReceived(ParkingListModel parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public ParkingListModel getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingListModel parkingSpot) {
        this.parkingSpot = parkingSpot;
    }
}
