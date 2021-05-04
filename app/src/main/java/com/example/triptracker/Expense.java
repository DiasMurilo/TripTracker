package com.example.triptracker;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

 /** <h1>Expense: Class to struct the image data for Firebase </h1>
 * <p>This activity is used to structure the data before send to database
      * more info https://firebase.google.com/docs
 * <p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */

public class Expense {
    /**name of the image and reference at the database*/
    public String imageRef;
    /**value of the expense*/
    public String value;
    /**Any description that user judge useful*/
    public String description;

    /**Default constructor required for calls to DataSnapshot.getValue(User.class)*/
    public Expense() {
    }

    /**
     * Method to prepare data to before send to database
     * @param imageRef name of the image and reference at the database
     * @param value value of the expense
     * @param description Any description that user judge useful
     */
    public Expense(String imageRef, String value, String description) {
        this.imageRef = imageRef;
        this.value = value;
        this.description = description;
    }
    /** method to exclude data from database, not being used at the moment (for future use)*/
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("imageRef", imageRef);
        result.put("value", value);
        result.put("description", description);
        return result;
    }
}