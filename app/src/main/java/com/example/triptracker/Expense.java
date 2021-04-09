package com.example.triptracker;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Activity to structure the image data for database
 *
 * This activity is used to structure the data before send to database
 *
 * @author Murilo Dias
 * @version 1.0
 * @since 2021.04.12
 */

public class Expense {
    public String imageRef;
    public String value;
    public String description;

    public Expense() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Expense(String imageRef, String value, String description) {
        //this.email = email;
        this.imageRef = imageRef;
        this.value = value;
        this.description = description;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("imageRef", imageRef);
        result.put("value", value);
        result.put("description", description);
        return result;
    }
}