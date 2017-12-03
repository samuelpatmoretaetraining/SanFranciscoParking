package com.muelpatmore.sanfranciscoparking.data.messages;

/**
 * Created by Samuel on 03/12/2017.
 */

public class ParkingSpotReservedConfirmation {
    private final boolean reservationStatus;
    private final String reservedUntil;
    private final int id;

    public ParkingSpotReservedConfirmation(boolean reservationStatus, String reservedUntil, int id) {
        this.reservationStatus = reservationStatus;
        this.reservedUntil = reservedUntil;
        this.id = id;
    }

    public boolean isReservationStatus() {
        return reservationStatus;
    }

    public String getReservedUntil() {
        return reservedUntil;
    }

    public int getId() {
        return id;
    }
}
