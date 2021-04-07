package com.example.triptracker;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class TripData {
    public String name;
    public String date;
    public String company;
    public String carRef;
    public String kml;
    public String fuel;
    public String reason;
    public String destination;
    public String distance;
    public TripData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TripData(String name, String date, String company, String carRef, String kml, String fuel, String reason, String destination, String distance) {
        //this.email = email;
        this.name = name;
        this.date = date;
        this.company = company;
        this.carRef = carRef;
        this.kml = kml;
        this.fuel = fuel;
        this.reason = reason;
        this.destination = destination;
        this.distance = distance;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("email", email);
        result.put("name", name);
        result.put("date", date);
        result.put("company", company);
        result.put("carRef", carRef);
        result.put("kml", kml);
        result.put("fuel", fuel);
        result.put("reason", reason);
        result.put("destination", destination);
        result.put("distance", distance);
        return result;
    }
}
