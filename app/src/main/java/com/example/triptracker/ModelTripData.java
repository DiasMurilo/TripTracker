package com.example.triptracker;

/** <h1>ModelTripData: Class that structure data to recycler view</h1>
 * <p>This class structures data to put in a view holder to build the recycler view <p>
 * Citation:
 * Class contains code adapted from
 * URL: https://firebase.google.com/docs/database
 * Permission:  Creative Commons Attribution 4.0 License under the Apache 2.0 License
 * Retrieved on: 28 Feb 2021
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class ModelTripData {

    /**String refer to database info*/
    String tripRef,date,reason,destination;

    /**Default constructor required for calls to DataSnapshot.getValue(User.class)*/
    ModelTripData()
    {     }

    /**
     * Method to prepare data to before send to database
     * @param date Date of the trip
     * @param reason Reason of the trip
     * @param destination Destination
     * @param tripRef Reference created based on the view position
     */
    public ModelTripData(String tripRef, String date, String reason, String destination) {
        this.tripRef = tripRef;
        this.date = date;
        this.reason = reason;
        this.destination = destination;
    }

    /**method returns trip reference*/
    public String getTripRef() {
        return tripRef;
    }

    /**Method returns Date*/
    public String getDate() {
        return date;
    }
    /**Method returns reason*/
    public String getReason() {
        return reason;
    }
    /**Method returns destination*/
    public String getDestination() {
        return destination;
    }

}
