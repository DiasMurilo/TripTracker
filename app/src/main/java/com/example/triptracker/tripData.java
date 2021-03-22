package com.example.triptracker;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class tripData {

    public String email;
    public String name;
    public String company;
    public String carRef;
    public float kml;
    public String fuel;
    public String reason;
    public String destiny;
    public float distance;
    public float fuelCons;
    public Date date;

    public tripData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public tripData(String email, String name, Date date, String company, String carRef, float kml, String fuel, String reason, String destiny, float distance, float fuelCons) {
        this.email = email;
        this.name = name;
        this.date = date;
        this.company = company;
        this.carRef = carRef;
        this.kml = kml;
        this.fuel = fuel;
        this.reason = reason;
        this.destiny = destiny;
        this.distance = distance;
        this.fuelCons = fuelCons;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);
        result.put("date", date);
        result.put("company", company);
        result.put("carRef", carRef);
        result.put("kml", kml);
        result.put("fuel", fuel);
        result.put("reason", reason);
        result.put("destiny", destiny);
        result.put("distance", distance);
        result.put("fuelCons", fuelCons);
        return result;
    }
}
