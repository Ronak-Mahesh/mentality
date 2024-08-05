package com.example.mentality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Select the "Journal" item by default
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;


            if (item.getItemId() == R.id.home) {
                intent = new Intent(MainActivity.this, MainActivity.class);
            } else if (item.getItemId() == R.id.journal) {
                intent = new Intent(MainActivity.this, JournalEntry.class);
            } else if (item.getItemId() == R.id.trends) {
                intent = new Intent(MainActivity.this, MoodTrends.class);
            } else if (item.getItemId() == R.id.mood) {
                intent = new Intent(MainActivity.this, DailyMood.class);
            } else if (item.getItemId() == R.id.resources) {
                intent = new Intent(MainActivity.this, MentalHealthResources.class);
            }


            if (intent != null) {
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });

    }
    public void OnMoodGraphClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, MoodGraph.class);
        MainActivity.this.startActivity(intent);
    }

    public void OnMoodTrendsClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, MoodTrends.class);
        MainActivity.this.startActivity(intent);
    }

    public void OnAddJournalClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, JournalEntry.class);
        MainActivity.this.startActivity(intent);
    }

    public void OnRateMoodClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, DailyMood.class);
        MainActivity.this.startActivity(intent);
    }

    public void OnButtonResourcesClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, MentalHealthResources.class);
        MainActivity.this.startActivity(intent);
    }
}