package com.example.mentality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MentalHealthResources extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health_resources);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Select the "Journal" item by default
        bottomNavigationView.setSelectedItemId(R.id.resources);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.home) {
                intent = new Intent(MentalHealthResources.this, MainActivity.class);
            } else if (item.getItemId() == R.id.journal) {
                intent = new Intent(MentalHealthResources.this, JournalEntry.class);
            } else if (item.getItemId() == R.id.trends) {
                intent = new Intent(MentalHealthResources.this, MoodTrends.class);
            } else if (item.getItemId() == R.id.mood) {
                intent = new Intent(MentalHealthResources.this, DailyMood.class);
            } else if (item.getItemId() == R.id.resources) {
                intent = new Intent(MentalHealthResources.this, MentalHealthResources.class);
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