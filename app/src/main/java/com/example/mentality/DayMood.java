package com.example.mentality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * This class is used to display the mood rating data for a day selected from moodtrends

 */
public class DayMood extends AppCompatActivity {

    MoodRating dayMoodRating;//The mood rating for the day
    TextView moodRatingText;//The text view for the mood rating
    TextView sleepText;//The text view for the sleep
    TextView glassesDrunkText;//The text view for the glasses drunk
    TextView weatherText;//The text view for the weather
    TextView journalText;//The text view for the journal entry
    ProgressBar moodRatingBar;//The progress bar for the mood rating

    /**
     * Initialises the activity and sets the text views to the data for the day selected
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_mood);
        dayMoodRating= (MoodRating) getIntent().getSerializableExtra("moodRating");
        if(dayMoodRating!=null)
        {
            System.out.println("Mood Rating: "+dayMoodRating.getMoodRating());//Debugging
            System.out.println("Glasses drunk: "+dayMoodRating.getGlassesDrunk());//Debugging
        }
        else
        {
            System.out.println("No mood rating for this date");
        }
        journalText = findViewById(R.id.journalDay);//Initialises the journal text view
        sleepText = findViewById(R.id.sleepText);//Initialises the sleep text view
        weatherText = findViewById(R.id.weatherForDay);//Initialises the weather text view
        weatherText.setText(getString(R.string.weather_day_text, dayMoodRating.getWeather()));//Sets the weather text view to the weather for the day selected
        sleepText.setText(getString(R.string.time_slept, dayMoodRating.getHoursSlept(),dayMoodRating.getMinutesSlept()));//Sets the sleep text view to the time slept for the day selected
        moodRatingBar = findViewById(R.id.progressBar);//Initialises the mood rating progress bar
        glassesDrunkText = findViewById(R.id.glassesDrunk);//Initialises the glasses drunk text view
        glassesDrunkText.setText(getString(R.string.glasses_drunk_text, dayMoodRating.getGlassesDrunk()));//Sets the glasses drunk text view to the glasses drunk for the day selected
        moodRatingText = findViewById(R.id.moodRatingText);//Initialises the mood rating text view
        moodRatingText.setText(getString(R.string.mood_rating_text, dayMoodRating.getMoodRating()));//Sets the mood rating text view to the mood rating for the day selected
        moodRatingBar.setProgress(Integer.parseInt(dayMoodRating.getMoodRating()));//Sets the progress bar to the mood rating for the day selected
        String journalPath = getIntent().getStringExtra("journalPath");//Gets the journal path from the intent so the text entered can be read from the file
        if(journalPath!=null)//If the journal path isn't null
        {
            File journalFile = new File(getFilesDir(), journalPath);//Creates a file object for the journal file
            if(journalFile.exists())//Checks if the journal file exists
            {
                try
                {
                    BufferedReader br = new BufferedReader(new FileReader(journalFile));//Creates a buffered reader to read the journal file
                    String line;//stores the line read from the file
                    StringBuilder text = new StringBuilder();//Stores the text read from the file
                    while ((line = br.readLine()) != null)
                    {
                        text.append(line);//Adds the line read from the file to the text
                        text.append('\n');//Adds a new line to the text
                    }
                    br.close();//Closes the buffered reader
                    journalText.setText(getString(R.string.Journal_entry,text.toString()));//Sets the journal text view to the text read from the file
                }
                catch (Exception e)
                {
                    System.out.println("Error reading journal file");//Debugging
                }
            }
            else
            {
                System.out.println("Journal file doesn't exist "+journalFile.getAbsolutePath());//Debugging
            }
        }
        else
        {
            System.out.println("Journal path is null");//Debugging
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Select the "Journal" item by default
        bottomNavigationView.setSelectedItemId(R.id.trends);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.home) {
                intent = new Intent(DayMood.this, MainActivity.class);
            } else if (item.getItemId() == R.id.journal) {
                intent = new Intent(DayMood.this, JournalEntry.class);
            } else if (item.getItemId() == R.id.trends) {
                intent = new Intent(DayMood.this, MoodTrends.class);
            } else if (item.getItemId() == R.id.mood) {
                intent = new Intent(DayMood.this, DailyMood.class);
            } else if (item.getItemId() == R.id.resources) {
                intent = new Intent(DayMood.this, MentalHealthResources.class);
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });

    }
}