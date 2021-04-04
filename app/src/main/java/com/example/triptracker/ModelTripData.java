package com.example.triptracker;

public class ModelTripData {

    String tripRef,date,reason,destination;
    ModelTripData()
    {     }
    public ModelTripData(String tripRef, String date, String reason, String destination) {
        this.tripRef = tripRef;
        this.date = date;
        this.reason = reason;
        this.destination = destination;
    }

    public String getTripRef() {
        return tripRef;
    }

    public void setTripRef(String tripRef) {
        this.tripRef = tripRef;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


}
