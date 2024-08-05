package com.example.mentality;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Ronak Mahesh
 * @author Igor Barglowski
 *
 * This class is used to add and edit a user's daily journal entry
 */
public class JournalEntry extends AppCompatActivity
{
    // Request ID for picking images
    private static final int PICK_IMAGE_REQUEST = 1;

    // File variable pointing to the app's storage directory
    File internalStorageDir;

    // TextView to display/edit users' journal entries
    TextView journalEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Select the "Journal" item by default
        bottomNavigationView.setSelectedItemId(R.id.journal);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;


            if (item.getItemId() == R.id.home) {
                intent = new Intent(JournalEntry.this, MainActivity.class);
            } else if (item.getItemId() == R.id.journal) {
                intent = new Intent(JournalEntry.this, JournalEntry.class);
            } else if (item.getItemId() == R.id.trends) {
                intent = new Intent(JournalEntry.this, MoodTrends.class);
            } else if (item.getItemId() == R.id.mood) {
                intent = new Intent(JournalEntry.this, DailyMood.class);
            } else if (item.getItemId() == R.id.resources) {
                intent = new Intent(JournalEntry.this, MentalHealthResources.class);
            }


            if (intent != null) {
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });

        // Create a new directory to store journal entries
        internalStorageDir = new File(getFilesDir() + "/mentalityJournals");
        internalStorageDir.mkdirs();

        journalEntry = findViewById(R.id.JournalText);

        try
        {
            // Get current date
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = dateFormat.format(calendar.getTime());

            String filepath = getFilesDir()+"/mentalityJournals/"+date+"-JournalEntry.txt";
            File file = new File(filepath);
            FileInputStream fileReader = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));
            String line;

            StringBuilder stringBuilder = new StringBuilder();

            // Load the journal entry for the current day if it exists
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();
            String fileContent = stringBuilder.toString();

            journalEntry.setText(fileContent);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        // Retrieve the image path from SharedPreferences in onCreate or onResume method
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imagePath = preferences.getString("imagePath", null);

        // Set the image to the ImageView if the imagePath is not null
        if (imagePath != null)
        {
            int resourceId = getResources().getIdentifier(imagePath, "drawable", getPackageName());
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(Uri.parse(imagePath));
        }
    }
    public void onSaveClick(View v)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(calendar.getTime());

        journalEntry = findViewById(R.id.JournalText);
        String entryText = journalEntry.getText().toString();

        String filename = date+"-JournalEntry.txt";
        File file = new File(internalStorageDir, filename);

        try
        {
            // Create a FileWriter and write the content to the file
            FileWriter writer = new FileWriter(file, false);
            writer.write(entryText+"\n");
            writer.flush();
            writer.close();
            Log.d("FileWriter", "Successfully Saved File");
            // The file has been successfully saved, so notify the user
            Toast.makeText(this, "Journal Entry Saved!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Log.d("FileWriter", "Failed To Save File");
            // Handle errors, such as unable to write to file
            e.printStackTrace();
        }
    }

    @SuppressLint("IntentReset")
    public void onUploadClick(View v)
    {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri selectedImageUri = data.getData();

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(selectedImageUri);

            // Get the drawable from the ImageView
            Drawable drawable = imageView.getDrawable();

            // Convert the drawable to a Bitmap
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Define the file path and name
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = dateFormat.format(calendar.getTime());
            String fileName = "journalImage"+date+".png";
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

            try
            {
                // Create a FileOutputStream to write the bitmap to the file
                FileOutputStream fos = new FileOutputStream(file);

                // Compress the bitmap to PNG format and write it to the FileOutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                // Flush and close the FileOutputStream
                fos.flush();
                fos.close();

                // Notify the system that a new file was saved (optional)
                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);

                // Provide feedback to the user that the image was saved successfully (optional)
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                // Save the image path to SharedPreferences when the image is set to ImageView
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                String fileName2 = "journal_image"+date+".png";

                editor.putString("imagePath", fileName2);
                editor.apply();
            }
            catch (IOException e)
            {
                // Handle error if something goes wrong during the save process
                e.printStackTrace();
            }

        }
    }
}