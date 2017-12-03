package com.muelpatmore.sanfranciscoparking.messages;

/**
 * Created by Samuel on 03/12/2017.
 */

public class ParkingSpotReservedConfirmation {
    private final boolean reservationStatus;
    private final String reservedUntil;

    public ParkingSpotReservedConfirmation(boolean reservationStatus, String reservedUntil) {
        this.reservationStatus = reservationStatus;
        this.reservedUntil = reservedUntil;
    }

    public boolean isReservationStatus() {
        return reservationStatus;
    }

    public String getReservedUntil() {
        return reservedUntil;
    }
}
