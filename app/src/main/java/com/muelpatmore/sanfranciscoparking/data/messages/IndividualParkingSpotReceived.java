package com.muelpatmore.sanfranciscoparking.data.messages;


import com.muelpatmore.sanfranciscoparking.data.network.networkmodels.ParkingSpaceModel;

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
