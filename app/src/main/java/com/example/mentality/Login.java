package com.example.mentality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is the login screen for the app. It is the first screen that the user sees when they open the app.
 */
public class Login extends AppCompatActivity {
boolean pinSet=false;//if pin is set
String pinHash;//hash of pin

    /**
     * This method is called when the activity is starting. It is used to set the content view to the login screen.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText pinmessage=findViewById(R.id.editTextNumberPassword);//pin message
        if (getSharedPreferences("pin", MODE_PRIVATE).getString("pinHash", "").equals("")) {//Checks if a pin is set
            pinmessage.setHint("Please set a pin");//Sets the hint of the pin field to "Please set a pin"
            pinSet=false;//Sets pinSet to false
        }
        else
        {
            pinSet=true;//Sets pinSet to true
            pinmessage.setHint(R.string.please_enter_your_pin);//Sets the hint of the pin field to "Please enter your pin"

        }
        pinmessage.addTextChangedListener(new TextWatcher() {//Adds a text watcher to the pin field
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==4){//If 4 digits have been entered
                    OnLoginClick(pinmessage);//Calls the OnLoginClick method
                    return;
            }


        }});

    }

    /**
     * This method is called when the user clicks the login button. It checks if the pin is correct and if it is, it opens the main activity.
     * @param v the view
     */
    public void OnLoginClick(View v){
        if(pinSet){
            EditText pinmessage=findViewById(R.id.editTextNumberPassword);//Gets the field where the user enters the pin
            pinHash=String.valueOf(pinmessage.getText().toString().hashCode());//Hashes the pin
            if(pinHash.equals(getSharedPreferences("pin", MODE_PRIVATE).getString("pinHash", ""))){//Checks if the hash of the pin is equal to the hash of the pin stored in the shared preferences
                Intent intent = new Intent(Login.this, MainActivity.class);//Opens the main activity
                Login.this.startActivity(intent);//Starts the main activity

            }
            else{
                Toast.makeText(this, "Incorrect pin", Toast.LENGTH_SHORT).show();//Displays a toast message saying that the pin is incorrect
                pinmessage.setText("");//Clears the pin field
                //pinmessage2.setText("Incorrect pin");
            }
        }
        else{
            EditText pinmessage=findViewById(R.id.editTextNumberPassword);//Gets the field where the user enters the pin
            pinHash=String.valueOf(pinmessage.getText().toString().hashCode());//Hashes the pin
            getSharedPreferences("pin", MODE_PRIVATE).edit().putString("pinHash", pinHash).apply();//Stores the hash of the pin in the shared preferences
            pinSet=true;//Sets pinSet to true
            pinmessage.setHint(R.string.please_enter_your_pin);//Sets the hint of the pin field to "Please enter your pin"
            pinmessage.setText("");//Clears the pin field
            this.recreate();//Recreates the activity
        }

    }
}