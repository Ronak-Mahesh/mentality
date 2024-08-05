package com.example.mentality;
import android.util.Log;

import java.io.Serializable;

 /*
 * This class stores the values that the user inputs when rating their daily mood.
 * This class is then serialised and stored so that it can be accessed throughout the day
 * even after the application has been closed, and it's data can be used for to asses
 * trends in mood overtime
 * */
public class MoodRating implements Serializable
 {

    // A rating of the users mood (0-10)
    private String moodRating;
    // The number of water glasses drunk
    private String glassesDrunk;
    // The recorded weather
    private String weather;
    // The number of minutes that the user slept for (0-59)
    private String minutesSlept;
    // The number of hours that the user slept for
    private String hoursSlept;

    /*
    * Constructor to initialise member variables
    * */
    public MoodRating()
    {
        moodRating = Integer.toString(0);
        glassesDrunk = Integer.toString(0);
        weather = null;
        minutesSlept = null;
        hoursSlept = null;
        Log.d("moodRatinObf", "created");
    }

    /*
    * Returns the number of hours the user slept for
    * */
    public String getHoursSlept()
    {
        return hoursSlept;
    }

    /*
    * Sets the number of hours the user slept for
    * */
    public void setHoursSlept(String hoursSlept)
    {
        this.hoursSlept = hoursSlept;
    }

    /*
    * Returns the number of minutes the user slept for
    * */
    public String getMinutesSlept()
    {
        return minutesSlept;
    }

    /*
    * Sets the number of minutes the user slept for
    * */
    public void setMinutesSlept(String minutesSlept)
    {
        this.minutesSlept = minutesSlept;
    }

    /*
    * Returns the weather that the user recorded
    * */
    public String getWeather()
    {
        return weather;
    }

    /*
    * Sets the weather that the user records
    * */
    public void setWeather(String weather)
    {
        this.weather = weather;
    }

    /*
    * Returns the number of glasses of water the user drank
    * */
    public String getGlassesDrunk()
    {
        return glassesDrunk;
    }

    /*
    * Sets the number of glasses of water the user drank
    * */
    public void setGlassesDrunk(String glassesDrunk)
    {
        this.glassesDrunk = glassesDrunk;
    }

    /*
    * Returns the number that the user rated their mood (0-10)
    * */
    public String getMoodRating()
    {
        return moodRating;
    }

    /*
    * Gets the number that the user rated their mood (0-10)
    * */
    public void setMoodRating(String moodRating)
    {
        this.moodRating = moodRating;
    }
}
