package com.example.triptracker;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** <h1>TripData: Class to struct the trip data for Firebase</h1>
 * <p>This activity is used to structure the data before send to database
 * more info https://firebase.google.com/docs
 * <p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */

@IgnoreExtraProperties
public class TripData {
    /**Name of the driver*/
    public String name;
    /**Date of the trip*/
    public String date;
    /**Company reference*/
    public String company;
    /**Car reference*/
    public String carRef;
    /**autonomy of the car km/l*/
    public String kml;
    /**Fuel type*/
    public String fuel;
    /**Reason of the trip*/
    public String reason;
    /**Destination*/
    public String destination;
    /**Distance of the trip*/
    public String distance;
    /**Path to the data in the database*/
    public HashMap<String, Expense> expenses;
    /**Default constructor required for calls to DataSnapshot.getValue(User.class)*/
    public TripData() {
    }
    /**
     * Method to prepare data to before send to database
     * @param name Name of the driver
     * @param date Date of the trip
     * @param company Company reference*
     * @param carRef Car reference
     * @param kml autonomy of the car km/l
     * @param fuel Fuel type
     * @param reason Reason of the trip
     * @param destination Destination
     * @param distance Path to the data in the database
     */
    public TripData(String name, String date, String company, String carRef, String kml, String fuel, String reason, String destination, String distance) {
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

    /** method to exclude data from database, not being used at the moment  (for future use)*/
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

    /**Method to get consumed fuel*/
    public float getConsumedFuel()
    {
        if (Float.parseFloat(kml) > 0)
        {
            return Float.parseFloat(distance)/Float.parseFloat(kml);
        }
        return  0;
    }

    /**Method to get the number of expenses in the trip*/
    public int getExpensesCount()
    {
        if (expenses != null)
        {
            return expenses.size();
        }
        return  0;
    }

    /**Method to get  total value of expenses per trip
     * @return*/
    public float getExpensesSum()
    {
        float total = 0;
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
    /**Method to  generate string to put in the report*/
    public String getExpenseInfo() {
        if (expenses != null && expenses.size() > 0) {
            return  "This trip has expenses, check expenses section on this report.";
        }
        return "";
    }

    /**method the reference of the expense images in database that refer to bucket*/
    public ArrayList<String> getExpensesImageReference()
    {
        ArrayList<String> refs = new ArrayList<>();
        for (Expense expense : getExpenses())
        {
            refs.add(expense.imageRef);
        }
        return refs;
    }

    /**Method to get expense image from bucket*/
    public Collection<Expense> getExpenses()
    {
        if (expenses == null)
        {
            return new ArrayList<Expense>();
        }
        return expenses.values();
    }
}
