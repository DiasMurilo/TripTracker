package com.example.triptracker;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

public class AddExpenses {
    public String imageRef;
    public String value;
    public String description;

    public AddExpenses() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public AddExpenses(String imageRef, String value, String description) {
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