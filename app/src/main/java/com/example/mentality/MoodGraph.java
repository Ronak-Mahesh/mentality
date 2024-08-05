package com.example.mentality;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity class to display certain attributes of the mood ratings in a line graph
 */
public class MoodGraph extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LineChart lineChart;
    List<String>dates = new ArrayList<>();
    List<MoodRating> ratings= new ArrayList<>();
    List<Entry>entries= new ArrayList<>();
    Spinner dropdown;
    String selectedAttribute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_graph);
        dropdown = findViewById(R.id.spinner);
        dropdown.setOnItemSelectedListener(this);
        lineChart = findViewById(R.id.moodChart);
        XAxis xAxis = lineChart.getXAxis();
        File directory = getFilesDir();
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".dat"));//Gets all files in the directory that end with .dat (All the mood rating files)
        Arrays.sort(files, (o1, o2) -> {
            Date date1 = getDateFromFileName(o1.getName());
            Date date2 = getDateFromFileName(o2.getName());

            // Compare dates
            return date1.compareTo(date2);//Sorts the files by date
        });

        populateDates(files);//Populates the dates list with the dates from the file names
        xAxis.setValueFormatter(new DateAxisValueFormatter(dates));//Sets the x axis labels to the dates
        xAxis.setLabelCount(dates.size(), true); // Set the label count
        read_files(files);//Reads the files in the directory and adds the mood ratings to the ratings list
        for (MoodRating rating : ratings) {
            System.out.println(rating.getMoodRating());//Debugging
        }
        LineDataSet lineDataSet = new LineDataSet(entries,selectedAttribute);//Creates a line data set with the entries list
        //List<Entry> entries = read_files(files);
        LineData lineData = new LineData(lineDataSet);//Creates a line data object with the line data set
        lineChart.setData(lineData);//Sets the line chart data to the line data object

    }

    /**
     * Gets the date from the file name
     * @param fileName Name of the file to extract the date from
     * @return A date object representing the date in the file name
     */
    private Date getDateFromFileName(String fileName) {
        String fileNameWithoutPrefix = fileName.replace("mood_rating_", "");//Removes the prefix from the file name
        String test = fileName.replace("mood_rating_", "");

        test = test.replace(".dat", "");//Removes the .dat from the file name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");//Creates a date format
        try {
            return dateFormat.parse(fileNameWithoutPrefix.substring(0, fileNameWithoutPrefix.lastIndexOf(".dat")));//Parses the date from the file name
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the files in the directory and adds the mood ratings to the ratings list
     * @param files Array of file paths to read the mood ratings from
     */
    private void read_files(File[] files) {
        //List<Entry> entries = new ArrayList<Entry>();
        for (File file : files) {

            // Read file
            try {
                ObjectInputStream ois = new ObjectInputStream(openFileInput(file.getName()));//Opens the file
                MoodRating moodRating = (MoodRating) ois.readObject();//Reads the mood rating from the file
                if (moodRating != null) {
                    // Add entry to list
                    ratings.add(moodRating);//Adds the mood rating to the ratings list
                    //entries.add(new Entry(count, Integer.parseInt(moodRating.getMoodRating())));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Populates the dates list with the dates from the file names
     * @param files Array of file paths to read the dates from
     */
    public void populateDates(File[] files){
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd", java.util.Locale.getDefault());
        for (File file : files) {
            String fileNameWithoutPrefix = file.getName().replace("mood_rating_", "");
            String test = file.getName().replace("mood_rating_", "");

            test = test.replace(".dat", "");//Removes the .dat from the file name
            try {
                Date date = dateFormat.parse(test);//Parses the date from the file name

                dates.add(outputFormat.format(date));//Adds the date to the dates list in the format dd/MM/yyyy
            } catch (Exception e) {
                // Handle parsing exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Populates the entries list with the mood ratings for the selected attribute
     * @param attribute The attribute to populate the entries list with
     */
    public void populateEntries(String attribute){

        int count=0;//Used to keep track of the x axis labels
        switch (attribute) {
            case "Mood Rating"://Populates the entries list with the mood ratings
                for (MoodRating rating : ratings) {
                    entries.add(new Entry(count, Integer.parseInt(rating.getMoodRating())));
                    Log.d("MoodGraph", "Rating: " + entries.size());
                    count++;
                }
                break;
            case "Glasses drunk"://Populates the entries list with the number of glasses drunk
                for (MoodRating rating : ratings) {
                    entries.add(new Entry(count, Integer.parseInt(rating.getGlassesDrunk())));
                    Log.d("MoodGraph", "Glasses: " + entries.size());
                    count++;
                }
                break;
            case "Hours Slept"://Populates the entries list with the number of hours slept
                for (MoodRating rating : ratings) {
                    int hours = 0;
                    int minutes = 0;
                    if(rating.getHoursSlept() != null){//If the hours slept is not null
                        hours = Integer.parseInt(rating.getHoursSlept());
                    }
                    else if(rating.getMinutesSlept() != null){//If the minutes slept is not null
                        minutes = Integer.parseInt(rating.getMinutesSlept());
                    }

                    float totalHours = hours + ((float)minutes / 60);//Calculates the total hours slept (converts minutes to hours)
                    entries.add(new Entry(count, totalHours));//Adds the total hours slept to the entries list
                    Log.d("MoodGraph", "Hours: " + entries.size());
                    count++;
                }
        }
                Log.d("MoodGraph", String.valueOf(entries.size()));
        }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedAttribute = parent.getSelectedItem().toString();//Gets the selected attribute from the spinner
        System.out.println(selectedAttribute);//Debugging
        System.out.println("Selected: " + selectedAttribute);//Debugging
        entries.clear();// Clear existing entries before populating new ones
        Log.d("MoodGraph", String.valueOf(entries.size()));//Debugging
        populateEntries(selectedAttribute);//Populates the entries list with the mood ratings for the selected attribute
        Log.d("MoodGraph", String.valueOf(entries.size()));//Debugging
        LineDataSet lineDataSet = new LineDataSet(entries, selectedAttribute);//Creates a line data set with the entries list
        LineData lineData = new LineData(lineDataSet);//Creates a line data object with the line data set
        lineChart.setData(lineData);//Sets the line chart data to the line data object
        lineChart.invalidate(); // Make sure to call invalidate after setting data

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    /**
     * Class to format the x-axis labels on the line graph to display the dates
     */
    private static class DateAxisValueFormatter extends ValueFormatter {
        private final List<String> dateLabels;

        DateAxisValueFormatter(List<String> dateLabels) {
            this.dateLabels = dateLabels;
        }//Constructor

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int index = (int) value;
            if (index >= 0 && index < dateLabels.size()) {
                return dateLabels.get(index);
            } else {
                return ""; // Handle out-of-bounds values
            }
        }
    }
}