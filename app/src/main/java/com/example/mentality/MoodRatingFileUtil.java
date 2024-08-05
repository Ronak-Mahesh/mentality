package com.example.mentality;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* This class will generate the filename used to store and access
* the serialised MoodRating data for the current day.
* The file format is mood_rating_yyyy_mm_d.dat
* */
public class MoodRatingFileUtil
{

    // Member variable to store the relevant filename for the current day
    private final String moodRatingFile;

    /*
    * Constructor, which initialises the moodRatingFile variable
    * */
    public MoodRatingFileUtil()
    {
        String FILE_PREFIX = "mood_rating_";
        String FILE_SUFFIX = ".dat";
        moodRatingFile = FILE_PREFIX.concat(getDate()).concat(FILE_SUFFIX);
    }

    /*
    * Returns the filename of the MoodRating file for the current day
    * */
    public String getMoodRatingFile()
    {
        return moodRatingFile;
    }

    /*
    * Returns the current date in the yyyy_mm_d format
    * */
    private String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_d");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
