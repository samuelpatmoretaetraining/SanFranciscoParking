package com.muelpatmore.sanfranciscoparking.messages;

import com.muelpatmore.sanfranciscoparking.networkmodels.ParkingSpaceModel;

/**
 * Created by Samuel on 02/12/2017.
 */

public class IndividualParkingSpotReceived {
    private ParkingSpaceModel parkingSpot;

    public IndividualParkingSpotReceived(ParkingSpaceModel parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public ParkingSpaceModel getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpaceModel parkingSpot) {
        this.parkingSpot = parkingSpot;
    }
}
