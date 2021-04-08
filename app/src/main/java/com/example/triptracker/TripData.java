package com.example.triptracker;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
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
    public HashMap<String, Expense> expenses;

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

    public float getConsumedFuel()
    {
        if (Float.parseFloat(kml) > 0)
        {
            return Float.parseFloat(distance)/Float.parseFloat(kml);
        }

        return  0;
    }

    public int getExpensesCount()
    {
        if (expenses != null)
        {
            return expenses.size();
        }

        return  0;
    }

    public int getExpensesSum()
    {
        int total = 0;
        if (expenses != null)
        {
            for (Expense expense : expenses.values())
            {
                if (!expense.value.isEmpty())
                {
                    total += Float.parseFloat(expense.value);
                }
            }
        }

        return  total;
    }

    public String getExpensesReference()
    {
        ArrayList<String> refs = new ArrayList<>();
        if (expenses != null)
        {
            for (Expense expense : expenses.values())
            {
                refs.add(expense.imageRef);
            }

            return TextUtils.join(", ", refs);
        }

        return "";
    }
}
