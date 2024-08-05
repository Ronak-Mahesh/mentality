package com.example.mentality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
* This activity allows the user to enter their mood metrics for the current day
* */
public class DailyMood extends AppCompatActivity
{

    // A mood rating object used to get/set the users inputted values
    private MoodRating moodRatingObj;
    // An instance of MoodRatingFileUtil used to access
    // the filepath to be used on the current day for file loading/saving
    private final MoodRatingFileUtil fileUtil = new MoodRatingFileUtil();


    /*
    * Initialises the components on screen with the correct values
    * and loads/generates the m_moodRatingObj
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_mood);

        initMoodFile();
        setUpWeatherSpinner();
        initGlassesDrunk();
        initSleepingText();
        initMoodRating();
    }

    /*
    * Creates / overwrites (if one exists) a file and stores the
    * serialised m_moodRatingObj in it
    * */
    private void createMoodRatingFile()
    {
        Log.d("createMoodRatingFile", "ran");
        String fileName = fileUtil.getMoodRatingFile();
        FileOutputStream fos;
        ObjectOutputStream os;

        try
        {
            Log.d("createMoodRatingFile", "writing object to file");
            fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(moodRatingObj);
            os.close();
            fos.close();
        }
        catch (IOException e)
        {
            Log.e("createMoodRatingFile", "Couldn't create mood file");
        }
    }

    /*
    * Loads (if one exists) the file for this day that is used to store the MoodRating object,
    * and places the contents in the m_moodRatingObj variable. If a file does not exist yet,
    * then createMoodRatingFile() will be called to create a new file and new MoodRating object
    * */
    private void initMoodFile()
    {
        Log.d("initMoodFile", "ran");
        String fileName = fileUtil.getMoodRatingFile();
        FileInputStream fis;
        ObjectInputStream is;

        try
        {
            fis = this.openFileInput(fileName);
        }
        catch (FileNotFoundException e)
        {
            Log.d("initMoodFile", "file not found");
            moodRatingObj = new MoodRating();
            createMoodRatingFile();
            return;
        }

        try
        {
            Log.d("initMoodFile", "creating mood file");
            is = new ObjectInputStream(fis);
            moodRatingObj = (MoodRating) is.readObject();
            is.close();
            fis.close();
        }
        catch (ClassNotFoundException | IOException e)
        {
            Log.d("exception", "ClassNotFoundException");
        }
    }

    /*
    * This method will initialise the mood seekbar component and moodRating text
    * so that the values they present reflect the values stored in m_moodRatingObj
    * */
    private void initMoodRating()
    {
        TextView moodRating = findViewById(R.id.moodRating);
        // Set the text of the moodRating component to reflect the value
        // stored in m_moodRatingObj
        String updatedText = getString(
                R.string.mood_rating).concat(moodRatingObj.getMoodRating());
        moodRating.setText(updatedText);

        SeekBar moodSeekbar = findViewById(R.id.seekBarMood);
        // Set the progress of the seekbar to reflect the value stored in m_moodRatingObj
        moodSeekbar.setProgress(Integer.parseInt(moodRatingObj.getMoodRating()));

        // Add a new listener to the seekbar to detect when the user changes its value
        moodSeekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {

                    /*
                    * Called when the user moves the seekbar. Stores the new value
                    * in the m_moodRatingObj and updates the MoodRating text accordingly
                    * */
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                    {
                        moodRatingObj.setMoodRating(Integer.toString(i));
                        TextView moodRating = findViewById(R.id.moodRating);
                        String updatedText = getString(
                                R.string.mood_rating).concat(Integer.toString(i));
                        moodRating.setText(updatedText);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar)
                    {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {

                    }
                }
        );
    }

    /*
     * This method will initialise the EditText components that allow the user
     * to enter the amount of minutes and hours they were asleep for, with the values
     * stored in the m_moodRatingObj. Adds InputFilters to prevent values that are invalid
     * hours (0-23), minutes (0-59)
     * */
    private void initSleepingText()
    {
        EditText minutesSlept = findViewById(R.id.minutesSleptNumber);
        minutesSlept.setText(moodRatingObj.getMinutesSlept());
        EditText hoursSlept = findViewById(R.id.hoursSleptNumber);
        hoursSlept.setText(moodRatingObj.getHoursSlept());
        minutesSlept.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
        hoursSlept.setFilters(new InputFilter[]{new InputFilterMinMax(0, 23)});
    }

    /*
    * This method will initialise the glasses drunk TextView component
    * with the value stored in the m_moodRatingObj
    * */
    private void initGlassesDrunk()
    {
        TextView glassesDrunk = findViewById(R.id.glassesDrunkText);
        String strGlassesDrunk = moodRatingObj.getGlassesDrunk();
        String updatedText = getString(R.string.glasses_drunk).concat(strGlassesDrunk);
        glassesDrunk.setText(updatedText);
    }

    /*
    * An personalised InputFilter object used to limit the valid input values
    * for the hours and minutes slept EditText components
    * */
    public static class InputFilterMinMax implements InputFilter
    {
        // The minimum allowed input
        private final int minimumValue;
        // The maximum allowed input
        private final int maximumValue;

        /*
        * Sets the minimum and maximum allowed input values
        * */
        public InputFilterMinMax(int minimumValue, int maximumValue)
        {
            this.minimumValue = minimumValue;
            this.maximumValue = maximumValue;
        }

        /*
        * Checks whether the given input is within the allowed minimum and maximum values
        * and then alters the value if not valid
        * */
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3)
        {
            try
            {
                int input = Integer.parseInt(spanned.subSequence(0, i2).toString() +
                        charSequence + spanned.subSequence(i3, spanned.length()));
                if (isInRange(minimumValue, maximumValue, input))
                    // Accept the input and leave it unchanged
                    return null;
            }
            catch (NumberFormatException e){}
            // Reject the input and change it to an empty string
            return "";
        }

        /*
         * Checks whether the given input is within the allowed minimum and maximum values
         * */
        private boolean isInRange(int a, int b, int c)
        {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    /*
    * Increments the m_moodRatingObj glasses drunk variable and
    * displays the new value in the relevant TextView
    * */
    public void onGlassesPlusClick(View v)
    {
        int glasses = Integer.parseInt(moodRatingObj.getGlassesDrunk());
        glasses++;
        Log.d("glasses", Integer.toString(glasses));
        moodRatingObj.setGlassesDrunk(Integer.toString(glasses));
        TextView glassesDrunk = findViewById(R.id.glassesDrunkText);
        String strGlassesDrunk = moodRatingObj.getGlassesDrunk();
        String updatedText = getString(R.string.glasses_drunk).concat(strGlassesDrunk);
        glassesDrunk.setText(updatedText);
    }


    /*
     * Decrements the m_moodRatingObj glasses drunk variable and
     * displays the new value in the relevant TextView
     * */
    public void onGlassesMinusClick(View v)
    {
        int glasses = Integer.parseInt(moodRatingObj.getGlassesDrunk());
        if(glasses > 0)
        {
            glasses--;
            moodRatingObj.setGlassesDrunk(Integer.toString(glasses));
            TextView glassesDrunk = findViewById(R.id.glassesDrunkText);
            String strGlassesDrunk = moodRatingObj.getGlassesDrunk();
            String updatedText = getString(R.string.glasses_drunk).concat(strGlassesDrunk);
            glassesDrunk.setText(updatedText);
        }
    }

    /*
    * Initialises the weather spinner component with the available weather options, and
    * sets the value displayed to be the same as the value store in the m_moodRatingObj
    * */
    private void setUpWeatherSpinner()
    {
        Spinner weatherSpinner = findViewById(R.id.todaysWeatherSpinner);
        ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.weather_options,
                android.R.layout.simple_spinner_item
        );
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(weatherAdapter);

        int defaultPos = weatherAdapter.getPosition(moodRatingObj.getWeather());
        weatherSpinner.setSelection(defaultPos);


        // Add an new listener for when an item is selected
        weatherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // When an item is selected update the m_moodRatingObj to reflect this
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                moodRatingObj.setWeather((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
    * Get the values entered in the minutes and hours EditText components and store
    * them in the m_moodRatingObj. Store all the values in the m_moodRatingObj to
    * the mood rating file.
    * */
    public void onSubmitClick(View v)
    {
        EditText minutesText = findViewById(R.id.minutesSleptNumber);
        EditText hoursText = findViewById(R.id.hoursSleptNumber);

        moodRatingObj.setMinutesSlept(minutesText.getText().toString());
        moodRatingObj.setHoursSlept(hoursText.getText().toString());

        createMoodRatingFile();

        // The mood has been successfully saved, so notify the user
        Toast.makeText(this, "Mood Saved!", Toast.LENGTH_SHORT).show();
    }
}