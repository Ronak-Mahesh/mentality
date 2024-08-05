package com.example.mentality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.ObjectInputStream;

/**
 * This class is used to display a calendar view for the user to view their mood ratings for different days
 */
public class MoodTrends extends AppCompatActivity
{
    CalendarView calendarView;//The calendar view for the user to select a date

    /**
     * Initialises the activity and sets the calendar view to handle the user pressing on a date
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_trends);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->//When a date is selected
        {
            System.out.println("Date: " + dayOfMonth + "/" + (month + 1) + "/" + year);//Debugging
            //Checks if a file exists for the selected date
            String dayOfMonthPadded = (dayOfMonth < 10) ? String.format("%02d", dayOfMonth) : String.valueOf(dayOfMonth);//Pads the day of month with a 0 if it is less than 10
            String jounralPath="mentalityJournals/"+dayOfMonthPadded+"-"+(month+1)+"-"+year+"-JournalEntry.txt";//The path to the journal file for the selected date
            int realMonth = month + 1;//CalendarView months start at 0
            String filePath = "mood_rating_" + year + "_" + realMonth + "_" + dayOfMonth + ".dat";//The path to the mood rating file for the selected date
            File file = new File(getFilesDir(), filePath);//Creates a file object for the mood rating file
            if (file.exists()) {//If the file exists
                try {
                    ObjectInputStream ois = new ObjectInputStream(openFileInput("mood_rating_" + year + "_" + realMonth + "_" + dayOfMonth + ".dat"));//Creates an object input stream for the mood rating file
                    MoodRating moodRating = (MoodRating) ois.readObject();//Reads the mood rating from the file
                    ois.close();
                    if (moodRating != null)//If a mood rating exists for the selected date
                    {
                        System.out.println("Mood Rating: " + moodRating.getMoodRating());//Debugging
                        System.out.println("Glasses Drunk: " + moodRating.getGlassesDrunk());//Debugging
                        Intent intent = new Intent(MoodTrends.this, DayMood.class);//Creates an intent to start the DayMood activity
                        intent.putExtra("moodRating", moodRating);//Adds the mood rating to the intent
                        intent.putExtra("journalPath",jounralPath);//Adds the journal path to the intent
                        startActivity(intent);//Starts the DayMood activity
                    } else {

                        System.out.println("No mood rating for this date");//Debugging
                    }
                } catch (Exception e)//If the file doesn't exist
                {
                    System.out.println("mood_rating_" + year + "_" + realMonth + "_" + dayOfMonth + ".dat");//Debugging
                    System.out.println("File not found");//Debugging
                }

            }
            else {
                System.out.println("File does not exist: " + file.getAbsolutePath());
            }
        });


    }

}